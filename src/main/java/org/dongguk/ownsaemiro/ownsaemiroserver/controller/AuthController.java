package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.AuthSignUpDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.OauthSignUpDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AvailableSerialIdDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.ServiceSerialIdDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 카카오 일반 사용자 로그인(회원가입 포함)
     */
    @GetMapping("/api/oauth/login/kakao")
    public ResponseDto<?> signUpKakao(@RequestParam("access_token") String accessToken){
        ServiceSerialIdDto kakaoSerialIdDto = authService.getKakaoSerialId(accessToken);

        return ResponseDto.ok(kakaoSerialIdDto);
    }

    /**
     * 네이버 일반 사용자 시리얼 아이디 조회
     */
    @GetMapping("/api/oauth/login/naver")
    public ResponseDto<?> getNaverSerialId(@RequestParam("access_token") String accessToken) throws IOException {
        ServiceSerialIdDto naverSerialId = authService.getNaverSerialId(accessToken);

        return ResponseDto.ok(naverSerialId);
    }

    /**
     * 사용자 회원가입 및 로그인 (oauth2)
     */
    @PostMapping("/api/oauth/sign-up")
    public void signUpOauth(HttpServletResponse response, @RequestBody OauthSignUpDto oauthSignUpDto) throws IOException {
        authService.signUpOauth(response, oauthSignUpDto);
    }

    /**
     * 판매자, 관리자용 회원가입 (폼 회원가입)
     */
    @PostMapping("/api/auth/sign-up")
    public ResponseDto<?> signUpDefault(@RequestBody AuthSignUpDto authSignUpDto){
        authService.signUpDefault(authSignUpDto);

        return ResponseDto.ok(null);
    }

    /**
     * 아이디 중복 확인
     */
    @GetMapping("/api/auth/check")
    public ResponseDto<?> checkSerialId(@RequestParam("serial_id") String newSerialId){

        AvailableSerialIdDto availableSerialIdDto = authService.isAvailableSerialId(newSerialId);

        return ResponseDto.ok(availableSerialIdDto);
    }


}
