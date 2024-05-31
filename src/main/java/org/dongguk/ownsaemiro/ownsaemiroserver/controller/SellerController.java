package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ApplyEventDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.EventRequestDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyAppliesDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.EventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {
    private final EventService eventService;
    @PostMapping("/apply")
    public ResponseDto<?> applyEvent(@UserId Long userId, @RequestBody ApplyEventDto applyEventDto){
        Event newEvent = eventService.saveEvent(userId, applyEventDto);
        EventRequestDto eventRequestDto = eventService.saveEventRequest(newEvent);

        return ResponseDto.ok(eventRequestDto);
    }

    @GetMapping("/apply")
    public ResponseDto<?> showApplies(@UserId Long userId, @RequestParam("page") Integer page, @RequestParam("size") Integer size){
        MyAppliesDto myAppliesDto = eventService.showMyApplies(userId, page-1, size);

        return ResponseDto.ok(myAppliesDto);
    }

    @GetMapping("/apply/search")
    public ResponseDto<?> showApplies(
            @UserId Long userId,
            @RequestParam("name") String name,
            @RequestParam("status") String status,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size){
        MyAppliesDto searchMyApplies = eventService.searchMyApplies(userId, name, status, page - 1, size);

        return ResponseDto.ok(searchMyApplies);
    }

    @DeleteMapping("/apply")
    public ResponseDto<?> cancelApply(@UserId Long userId, @RequestParam("request_id") Long eventRequestId){
        eventService.cancelApply(userId, eventRequestId);

        return ResponseDto.ok(null);
    }
}
