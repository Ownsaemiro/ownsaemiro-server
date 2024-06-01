package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ApplyEventDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.EventRequestDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyAppliesDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyEventHistoriesDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.EventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {
    private final EventService eventService;

    /*  판매자 판매 이력 api  */
    /**
     * 판매자 판매 이력 조회
     */
    @GetMapping("/histories")
    public ResponseDto<?> showSellerHistories(
            @UserId Long userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size ) {
        MyEventHistoriesDto myEventHistoriesDto = eventService.showMyHistories(userId, page-1, size);

        return ResponseDto.ok(myEventHistoriesDto);
    }

    /*  판매자 판매 요청 api  */
    /**
     * 판매자 판매 요청하기
     */
    @PostMapping("/apply")
    public ResponseDto<?> applyEvent(@UserId Long userId, @RequestBody ApplyEventDto applyEventDto){
        Event newEvent = eventService.saveEvent(userId, applyEventDto);
        EventRequestDto eventRequestDto = eventService.saveEventRequest(newEvent);

        return ResponseDto.ok(eventRequestDto);
    }

    /**
     * 판매자 판매 요청 목록보기
     */

    @GetMapping("/apply")
    public ResponseDto<?> showApplies(
            @UserId Long userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size){
        MyAppliesDto myAppliesDto = eventService.showMyApplies(userId, page-1, size);

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
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size){
        MyAppliesDto searchMyApplies = eventService.searchMyApplies(userId, name, status, page - 1, size);

        return ResponseDto.ok(searchMyApplies);
    }

    /**
     * 판매자 판매 요청 취소하기
     */

    @DeleteMapping("/apply")
    public ResponseDto<?> cancelApply(@UserId Long userId, @RequestParam("request_id") Long eventRequestId){
        eventService.cancelApply(userId, eventRequestId);

        return ResponseDto.ok(null);
    }
}
