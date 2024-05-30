package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserImage;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.UpdateNicknameDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserImageRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    public String getNickname(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        return user.getNickname();
    }

    @Transactional
    public void updateProfile(Long userId, MultipartFile image, UpdateNicknameDto updateNicknameDto) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 이미지를 변경 해야 하는 경우
        if (!image.isEmpty() && s3Service.readyForUpload(user, image)){
            String url = s3Service.uploadToS3(user, image);
            userImageRepository.save(
                    UserImage.builder()
                            .url(url)
                            .name(image.getOriginalFilename().split("\\.")[0] + user.getSerialId())
                            .createdAt(LocalDate.now())
                            .user(user)
                            .build()
            );
        }

        // 닉네임 변경하기
        user.updateNickname(updateNicknameDto.nickname());
    }

}
