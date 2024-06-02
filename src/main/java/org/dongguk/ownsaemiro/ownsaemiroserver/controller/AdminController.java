package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ChangeEventRequestStatusDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AdminApplyEventDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.ChangedEventRequestStatusDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.DetailOfEventRequestDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.EventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final EventService eventService;
    @GetMapping("/register")
    public ResponseDto<?> showAppliesOfSeller(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        AdminApplyEventDto adminApplyEventDto = eventService.showAppliesOfSeller(page-1, size);

        return ResponseDto.ok(adminApplyEventDto);
    }

    @GetMapping("/register/detail")
    public ResponseDto<?> showDetailOfApply(@RequestParam("event_id") Long eventRequestId){
        DetailOfEventRequestDto detailOfEventRequestDto = eventService.showDetailOfEventRequest(eventRequestId);

        return ResponseDto.ok(detailOfEventRequestDto);
    }

    @GetMapping("/register/search")
    public ResponseDto<?> searchEventRequest(
            @RequestParam("name") String name,
            @RequestParam("state") String state,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        AdminApplyEventDto adminApplyEventDto = eventService.searchEventRequest(name, state, page-1, size);

        return ResponseDto.ok(adminApplyEventDto);
    }

    @PatchMapping("/register")
    public ResponseDto<?> changeEventRequestStatus(@RequestBody ChangeEventRequestStatusDto changeEventRequestStatusDto){
        ChangedEventRequestStatusDto changedEventRequestStatusDto = eventService.changeEventRequestState(changeEventRequestStatusDto);

        return ResponseDto.ok(changedEventRequestStatusDto);
    }

}
