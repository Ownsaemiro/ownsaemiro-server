package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserWallet;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.AuthSignUpDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AvailableSerialIdDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.JwtTokenDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EProvider;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ERole;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserWalletRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.info.AuthenticationResponse;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.JwtUtil;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.RestClientUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserWalletRepository userWalletRepository;
    private final RestClientUtil restClientUtil;
    private final JwtUtil jwtUtil;

    /**
     * 카카오 로그인
     * @param response
     * @param accessToken
     */
    @Transactional
    public void signUpKakao(HttpServletResponse response, String accessToken) {
        // TODO: 사용자 정보 어떻게 받을지 논의 후 수정할 예정
        JSONObject jsonObject = restClientUtil.sendAppKakaoLoginRequest(Constants.KAKAO_LOGIN_PATH, accessToken);
        log.info(jsonObject.toJSONString());
    }

    /**
     * 네이버 로그인
     * @param response
     * @param accessToken
     * @throws IOException
     */

    @Transactional
    public void signUpNaver(HttpServletResponse response, String accessToken) throws IOException {
        // 네이버 서버에서 사용자 정보 가져오기
        Map<String, Object> naverResponse = restClientUtil.sendAppNaverLoginRequest(Constants.NAVER_LOGIN_PATH, accessToken);

        // DTO로 변경
        AuthSignUpDto authSignUpDto = AuthSignUpDto.resolveFromNaverInfo(new JSONObject(naverResponse));

        // 사용자 확인 -> 있으면 반환, 없으면 사용자 & 사용자 지갑 생성 후 반환
        User user = userRepository.findBySerialId(authSignUpDto.serialId())
                .orElseGet(() -> {
                            log.info("네이버 로그인, 신규 사용자 가입");
                            User newUser = userRepository.save(
                                    User.signUp(
                                            authSignUpDto,
                                            passwordEncoder.encode(authSignUpDto.password()),
                                            EProvider.NAVER,
                                            ERole.toERole(authSignUpDto.role())
                                    )
                            );
                            log.info("네이버 신규 사용자 지갑 생성 완료");
                            userWalletRepository.save(
                                    UserWallet.create(newUser.getId())
                            );
                            return newUser;
                    }
                );

        // 토큰 발행
        JwtTokenDto jwtTokenDto = jwtUtil.generateTokens(user.getId(), user.getRole());

        // response
        AuthenticationResponse.makeOauthSuccessResponse(response, jwtTokenDto);

    }

    /**
     * 폼 회원가입
     * @param authSignUpDto
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
    }

    public AvailableSerialIdDto isAvailableSerialId(String serialId){
        Boolean isAvailable = !userRepository.existsBySerialId(serialId);

        return AvailableSerialIdDto.builder()
                .available(isAvailable)
                .build();
    }

}
