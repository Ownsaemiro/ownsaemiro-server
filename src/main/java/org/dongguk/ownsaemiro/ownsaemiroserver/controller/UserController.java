package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.UpdateNicknameDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 사용자 닉네임 조회 api
     */
    @GetMapping("/nickname")
    public ResponseDto<?> getNickname(@UserId Long userId){
        String nickname = userService.getNickname(userId);
        return ResponseDto.ok(nickname);
    }

    /**
     * 사용자 프로필 업데이트 api
     */
    @PatchMapping
    public ResponseDto<?> updateProfile(
            @UserId Long userId,
            @RequestPart("image") MultipartFile image,
            @RequestPart("message") UpdateNicknameDto updateNicknameDto) throws IOException {

        userService.updateProfile(userId, image, updateNicknameDto);

        return ResponseDto.ok(null);
    }
}
