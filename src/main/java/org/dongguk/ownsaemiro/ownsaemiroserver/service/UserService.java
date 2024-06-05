package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Image;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserImage;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.BanInfo;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.UpdateNicknameDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserImageRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;

    /* ================================================================= */
    //                          관리자 api                                 //
    /* ================================================================= */
    /**
     * 관리자 정지 사용자 목록 조회
     */
    public ShowBannedUsersDto showBannedUsers(Integer page, Integer size){
        Page<User> allBannedUsers = userRepository.findAllByIsBanned(PageRequest.of(page, size));

        List<BanUserInfoDto> banUserInfoDtos = allBannedUsers.getContent().stream()
                .map(user -> BanUserInfoDto.builder()
                        .userId(user.getId())
                        .serialId(user.getSerialId())
                        .role(user.getRole().getRole())
                        .provider(user.getProvider().getProvider())
                        .build()
                ).toList();

        return ShowBannedUsersDto.builder()
                .pageInfo(PageInfo.convert(allBannedUsers, page))
                .banUserInfosDto(banUserInfoDtos)
                .build();
    }

    /**
     * 관리자 사용자 정지 or 정지 해제
     */
    @Transactional
    public BanUserInfoDto banUser(BanInfo banInfo){
        User user = userRepository.findBySerialId(banInfo.serialId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 이미 요청 상태와 동일한 경우
        if (user.getIsBanned().equals(banInfo.ban()))
            throw new CommonException(ErrorCode.ALREADY_SAME_BAN_INFO);

        Boolean userBan = user.ban();

        return BanUserInfoDto.builder()
                .userId(user.getId())
                .serialId(user.getSerialId())
                .role(user.getRole().getRole())
                .ban(userBan)
                .build();
    }

    /* ================================================================= */
    //                          사용자 api                                 //
    /* ================================================================= */
    public UserNicknameDto getNickname(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        return UserNicknameDto.builder()
                .nickname(user.getNickname())
                .build();
    }

    @Transactional
    public UserProfileDto updateProfile(Long userId, MultipartFile newImage, UpdateNicknameDto updateNicknameDto) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 이미지를 변경 해야 하는 경우
        if (!newImage.isEmpty() && s3Service.readyForUpload(user, newImage)){
            String url = s3Service.uploadToS3(user, newImage);
            userImageRepository.save(
                    UserImage.builder()
                            .url(url)
                            .name(newImage.getOriginalFilename().split("\\.")[0] + user.getSerialId())
                            .createdAt(LocalDate.now())
                            .user(user)
                            .build()
            );
        }

        // 닉네임 변경하기
        String nickname = user.updateNickname(updateNicknameDto.nickname());

        // 사용자 이미지 경로 가져오기
        String userImageUrl = userImageRepository.findByUser(user)
                .map(Image::getUrl)
                .orElse(Constants.DEFAULT_IMAGE);

        return UserProfileDto.builder()
                .url(userImageUrl)
                .nickname(nickname)
                .build();
    }

}
