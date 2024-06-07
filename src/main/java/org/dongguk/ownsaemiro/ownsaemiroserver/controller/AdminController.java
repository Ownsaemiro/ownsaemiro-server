package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.BanInfo;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ChangeEventRequestStatusDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.AdminService;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;

    /**
     * 관리자 닉네임 조회 api
     */
    @GetMapping("/nickname")
    public ResponseDto<?> getAdminNickname(@UserId Long userId){
        UserNicknameDto nickname = userService.getNickname(userId);

        return ResponseDto.ok(nickname);
    }

    /**
     * 관리자 정지된 사용자 목록 조회 api
     */
    @GetMapping("/banned")
    public ResponseDto<?> showBannedUsers(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        ShowBannedUsersDto showBannedUsersDto = userService.showBannedUsers(page - 1, size);

        return ResponseDto.ok(showBannedUsersDto);
    }

    /**
     * 관리자 사용자 관리 상태 변경 api
     */
    @PatchMapping("/banned")
    public ResponseDto<?> unBanUser(@RequestBody BanInfo banInfo){
        BanUserInfoDto banUserInfoDto = userService.banUser(banInfo);

        return ResponseDto.ok(banUserInfoDto);
    }

    /*  관리자 판매 요청 관련 api  */
    /**
     * 관리자 행사 등록 요청 목록 + 검색 api
     */
    @GetMapping("/register")
    public ResponseDto<?> showAppliesOfSeller(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        AdminApplyEventDto adminApplyEventDto = adminService.searchOrShowAppliesOfSeller(name, state, page-1, size);

        return ResponseDto.ok(adminApplyEventDto);
    }

    /**
     * 관리자 행사 등록 요청 상세 조회 api
     */
    @GetMapping("/register/detail")
    public ResponseDto<?> showDetailOfApply(@RequestParam("event_id") Long eventRequestId){
        DetailOfEventRequestDto detailOfEventRequestDto = adminService.showDetailOfEventRequest(eventRequestId);

        return ResponseDto.ok(detailOfEventRequestDto);
    }

    /**
     * 관리자 행사 등록 요청 승인 여부 결정 api
     */
    @PatchMapping("/register")
    public ResponseDto<?> changeEventRequestStatus(@RequestBody ChangeEventRequestStatusDto changeEventRequestStatusDto){
        ChangedEventRequestStatusDto changedEventRequestStatusDto = adminService.changeEventRequestState(changeEventRequestStatusDto);

        return ResponseDto.ok(changedEventRequestStatusDto);
    }

}
