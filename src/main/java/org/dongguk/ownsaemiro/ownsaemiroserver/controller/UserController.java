package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.UpdateNicknameDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyPointDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.ParticipatedEventsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.UserProfileDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.UserService;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.UserTicketService;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.UserWalletService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserWalletService userWalletService;
    private final UserTicketService userTicketService;

    /**
     * 사용자 닉네임 조회 api
     */
    @GetMapping("/nickname")
    public ResponseDto<?> getNickname(@UserId Long userId){
        return ResponseDto.ok(userService.getNickname(userId));
    }

    /**
     * 사용자 프로필 업데이트 api
     */
    @PatchMapping
    public ResponseDto<?> updateProfile(
            @UserId Long userId,
            @RequestPart("image") MultipartFile image,
            @RequestPart("message") UpdateNicknameDto updateNicknameDto) throws IOException {

        UserProfileDto userProfileDto = userService.updateProfile(userId, image, updateNicknameDto);

        return ResponseDto.ok(userProfileDto);
    }

    /**
     * 사용자 잔고 확인하기 api
     */
    @GetMapping("/wallets")
    public ResponseDto<?> checkMyPoint(@UserId Long userWalletId){
        MyPointDto myPointDto = userWalletService.checkMyPoint(userWalletId);

        return ResponseDto.ok(myPointDto);
    }

    /**
     * 사용자 잔고 충전하기 api
     */
    @PutMapping("/wallets")
    public ResponseDto<?> rechargePoint(@UserId Long userWalletId, @RequestBody MyPointDto rechargePointDto){
        MyPointDto myPointDto = userWalletService.rechargePoint(userWalletId, rechargePointDto);

        return ResponseDto.ok(myPointDto);
    }
    /**
     * 사용자가 참여한 행사 목록 api
     */
    @GetMapping("/events/participate")
    public ResponseDto<?> showParticipatedEvents(
        @UserId Long userId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        ParticipatedEventsDto participatedEventsDto = userTicketService.showParticipatedEvents(userId, page - 1, size);

        return ResponseDto.ok(participatedEventsDto);
    }
}
