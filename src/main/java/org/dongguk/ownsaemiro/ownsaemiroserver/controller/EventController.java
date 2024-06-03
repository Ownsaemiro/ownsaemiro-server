package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import com.amazonaws.Response;
import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.EventsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.SearchEventsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    @GetMapping("/search")
    public ResponseDto<?> searchEvents(
            @RequestParam("name") String name,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        SearchEventsDto searchEventsDto = eventService.searchEvent(name, page-1, size);

        return ResponseDto.ok(searchEventsDto);
    }

    @GetMapping
    public ResponseDto<?> showEvents(
            @RequestParam("status") String status,
            @RequestParam("filter") String process,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        EventsDto eventsDto = eventService.showEvents(status, process, page, size);

        return ResponseDto.ok(eventsDto);
    }
}
