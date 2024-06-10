package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserImage;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserWallet;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.AuthSignUpDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.OauthSignUpDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AvailableSerialIdDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.JwtTokenDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.ServiceSerialIdDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EProvider;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ERole;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserImageRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserWalletRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.AuthUtil;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.JwtUtil;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.RestClientUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RestClientUtil restClientUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserWalletRepository userWalletRepository;
    private final UserImageRepository userImageRepository;



    /**
     * 카카오 시리얼 아이디 조회
     */
    public ServiceSerialIdDto getKakaoSerialId(String accessToken) {
        // 카카오 서버에서 사용자 정보 가져오기
        String kakaoSerialId = String.valueOf(restClientUtil.sendAppKakaoLoginRequest(Constants.KAKAO_LOGIN_PATH, accessToken).get("id"));

        return ServiceSerialIdDto.builder()
                .serialId(kakaoSerialId)
                .isExisted(userRepository.existsBySerialId(kakaoSerialId))
                .build();
    }

    /**
     * 네이버 시리얼 아이디 조회
     */
    public ServiceSerialIdDto getNaverSerialId(String accessToken) throws IOException {
        // 네이버 서버에서 사용자 정보 가져오기
        String naverSerialId = String.valueOf(restClientUtil.sendAppNaverLoginRequest(Constants.NAVER_LOGIN_PATH, accessToken).get("id"));

        return ServiceSerialIdDto.builder()
                .serialId(naverSerialId)
                .isExisted(userRepository.existsBySerialId(naverSerialId))
                .build();
    }

    /**
     * 폼 회원가입
     */
    @Transactional
    public void signUpDefault(AuthSignUpDto authSignUpDto){
        // 사용자 생성
        User user = userRepository.save(
                User.signUp(
                        authSignUpDto,
                        passwordEncoder.encode(authSignUpDto.password()),
                        EProvider.DEFAULT,
                        ERole.toERole(authSignUpDto.role())
                )
        );

        // 사용자 이미지 생성
        userImageRepository.save(
                UserImage.builder()
                        .name(Constants.DEFAULT_IMAGE_NAME)
                        .url(Constants.DEFAULT_IMAGE)
                        .createdAt(LocalDate.now())
                        .user(user)
                        .build()
        );
    }

    /**
     * oauth 회원가입 및 로그인
     */
    @Transactional
    public JwtTokenDto signUpOauth(OauthSignUpDto oauthSignUpDto){
        // 사용자 확인 -> 있으면 반환, 없으면 사용자 & 사용자 지갑 생성 후 반환
        User user = userRepository.findBySerialId(oauthSignUpDto.serialId())
                        .orElseGet(() -> {
                            User newUser = userRepository.save(User.signUp(
                                    oauthSignUpDto,
                                    passwordEncoder.encode(AuthUtil.makePassword()),
                                    EProvider.toEnum(oauthSignUpDto.provider()),
                                    ERole.USER
                            ));
                            log.info("oauth 새로운 사용자 저장 완료");

                            userWalletRepository.save(
                                    UserWallet.create(newUser.getId())
                            );
                            log.info("oauth 새로운 사용자 지갑 생성 완료");

                            userImageRepository.save(
                                    UserImage.builder()
                                            .name(Constants.DEFAULT_IMAGE_NAME)
                                            .url(Constants.DEFAULT_IMAGE)
                                            .createdAt(LocalDate.now())
                                            .user(newUser)
                                            .build()
                            );
                            log.info("oauth 사용자 이미지 생성");

                            return newUser;
                        });
        // 토큰 발행
        return jwtUtil.generateTokens(user.getId(), user.getRole());
    }

    /**
     * 아이디 중복 확인
     */
    public AvailableSerialIdDto isAvailableSerialId(String serialId){
        Boolean isAvailable = !userRepository.existsBySerialId(serialId);

        return AvailableSerialIdDto.builder()
                .available(isAvailable)
                .build();
    }

}
