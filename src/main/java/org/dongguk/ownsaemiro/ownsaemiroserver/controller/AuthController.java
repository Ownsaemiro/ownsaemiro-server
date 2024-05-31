package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.AuthSignUpDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AvailableSerialIdDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 카카오 일반 사용자 로그인(회원가입 포함)
     */
    @GetMapping("/oauth/login/kakao")
    public ResponseDto<?> signUpKakao(HttpServletResponse response, @RequestParam String accessToken){
        // TODO: 사용자 정보 어떻게 받을지 논의 후 수정할 예정

        authService.signUpKakao(response, accessToken);

        return ResponseDto.ok(null);
    }

    /**
     * 네이버 일반 사용자 로그인(회원 가입 포함)
     */
    @GetMapping("/oauth/login/naver")
    public void signUpNaver(HttpServletResponse response,  @RequestParam String accessToken) throws IOException {
        authService.signUpNaver(response, accessToken);
    }

    /**
     * 판매자, 관리자용 회원가입
     */
    @PostMapping("/api/auth/sign-up")
    public ResponseDto<?> signUpDefault(@RequestBody AuthSignUpDto authSignUpDto){
        authService.signUpDefault(authSignUpDto);

        return ResponseDto.ok(null);
    }

    /**
     * 아이디 중복 확인
     */
    @GetMapping("/check")
    public ResponseDto<?> checkSerialId(@RequestParam("serial_id") String newSerialId){

        AvailableSerialIdDto availableSerialIdDto = authService.isAvailableSerialId(newSerialId);

        return ResponseDto.ok(availableSerialIdDto);
    }


}
