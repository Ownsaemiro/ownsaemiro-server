package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ApplyEventDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ChangeSellingEventStatusDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.SellerEventService;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {
    private final UserService userService;
    private final SellerEventService sellerEventService;

    /**
     * 판매자 닉네임 조회 api
     */
    @GetMapping("/nickname")
    public ResponseDto<?> getSellerNickname(@UserId Long userId){
        UserNicknameDto nickname = userService.getNickname(userId);

        return ResponseDto.ok(nickname);
    }

    /*  판매자 판매 이력 api  */
    /**
     * 판매자 판매 이력 조회
     */
    @GetMapping("/histories")
    public ResponseDto<?> showSellerHistories(
            @UserId Long userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size ) {
        MyEventHistoriesDto myEventHistoriesDto = sellerEventService.showMyHistories(userId, page-1, size);

        return ResponseDto.ok(myEventHistoriesDto);
    }

    /**
     * 판매자 판매 행사 상태 변경
     */
    @PatchMapping("/events")
    public ResponseDto<?> changeEventStatus(
            @UserId Long userId,
            @RequestBody ChangeSellingEventStatusDto changeSellingEventStatusDto){
        ChangeEventStatusDto changeEventStatusDto = sellerEventService.changeEventStatus(userId, changeSellingEventStatusDto);

        return ResponseDto.ok(changeEventStatusDto);
    }

    /**
     * 판매자 판매 행사 검색
     */
    @GetMapping("/histories/search")
    public ResponseDto<?> searchMyEvent(
            @UserId Long userId,
            @RequestParam("name") String name,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        MyEventHistoriesDto myEventHistoriesDto = sellerEventService.searchMyEvents(userId, name, page, size);

        return ResponseDto.ok(myEventHistoriesDto);
    }


    /*  판매자 판매 요청 api  */
    /**
     * 판매자 판매 요청하기
     */
    @PostMapping("/apply")
    public ResponseDto<?> applyEvent(
            @UserId Long userId,
            @RequestPart("image") MultipartFile image,
            @RequestPart("data") ApplyEventDto applyEventDto) throws IOException {
        Event newEvent = sellerEventService.saveEvent(userId, image, applyEventDto);
        EventRequestDto eventRequestDto = sellerEventService.saveEventRequest(newEvent);

        return ResponseDto.ok(eventRequestDto);
    }

    /**
     * 판매자 판매 요청 목록보기
     */
    @GetMapping("/apply")
    public ResponseDto<?> showApplies(
            @UserId Long userId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size){
        MyAppliesDto myAppliesDto = sellerEventService.showMyApplies(userId, page-1, size);

        return ResponseDto.ok(myAppliesDto);
    }

    /**
     * 판매자 찬매 요청 검색하기
     */
    @GetMapping("/apply/search")
    public ResponseDto<?> showApplies(
            @UserId Long userId,
            @RequestParam("name") String name,
            @RequestParam("status") String status,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size){
        MyAppliesDto searchMyApplies = sellerEventService.searchMyApplies(userId, name, status, page - 1, size);

        return ResponseDto.ok(searchMyApplies);
    }

    /**
     * 판매자 판매 요청 취소하기
     */
    @DeleteMapping("/apply")
    public ResponseDto<?> cancelApply(@UserId Long userId, @RequestParam("request_id") Long eventRequestId){
        sellerEventService.cancelApply(userId, eventRequestId);

        return ResponseDto.ok(null);
    }
}
