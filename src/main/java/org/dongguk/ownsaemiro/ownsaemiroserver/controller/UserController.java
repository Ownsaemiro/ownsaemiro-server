package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.NotificationIdDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.UpdateNicknameDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.NotificationService;
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
    private final NotificationService notificationService;

    /**
     * 사용자 닉네임 조회 api
     */
    @GetMapping("/nickname")
    public ResponseDto<?> getNickname(@UserId Long userId){
        return ResponseDto.ok(userService.getNickname(userId));
    }

    /**
     * 사용자 프로필 이미지 조회 api
     */
    @GetMapping("/profile-image")
    public ResponseDto<?> getProfileImage(
            @UserId Long userId
    ) {
        return ResponseDto.ok(userService.getUserProfile(userId));
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
     * 사용자 FCM Token 저장하기
     */
    @PutMapping("/fcm-token")
    public ResponseDto<?> saveFCMTokenOfUser(@UserId Long userId, @RequestBody FCMTokenDto fcmTokenDto){
        userService.saveFCMToken(userId, fcmTokenDto);

        return ResponseDto.ok(null);
    }

    /**
     * 사용자 알림 목록 조회하기
     */
    @GetMapping("/notifications")
    public ResponseDto<?> showNotificationsOfUser(
            @UserId Long userId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size){
        NotificationsDto notificationsDto = notificationService.showNotificationsOfUser(userId, page-1, size);

        return ResponseDto.ok(notificationsDto);
    }

    /**
     * 사용자 알림 삭제하기(사용자 읽음)
     */
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseDto<?> deleteNotification(@PathVariable Long notificationId){
        notificationService.deleteNotification(notificationId);

        return ResponseDto.ok(null);
    }
}
