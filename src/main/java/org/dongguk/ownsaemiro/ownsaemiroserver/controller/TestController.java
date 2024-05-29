package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.TestAuthSignUpDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class TestController {
    private final AuthService authService;
    @PostMapping("/sign-up")
    public ResponseDto<?> signUp(@RequestBody TestAuthSignUpDto authSignUpDto){
        authService.signUp(authSignUpDto);
        return ResponseDto.ok(null);
    }
}
