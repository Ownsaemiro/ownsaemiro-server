package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.WriteReviewOfEvent;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.EventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    @PostMapping("{eventId}/like")
    public ResponseDto<?> saveUserLikedEvent(@UserId Long userId, @PathVariable Long eventId){
        LikedEventDto likedEventDto = eventService.userLikeEvent(userId, eventId);

        return ResponseDto.created(likedEventDto);
    }

    @GetMapping("/likes")
    public ResponseDto<?> showUserLikedEvents(
            @UserId Long userId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        UserLikedEventsDto userLikedEventsDto = eventService.showUserLikedEvents(userId, page-1, size);

        return ResponseDto.ok(userLikedEventsDto);
    }

    @DeleteMapping("{eventId}/like")
    public ResponseDto<?> deleteUserLikedEvent(@UserId Long userId, @PathVariable Long eventId){
        UnlikedEventDto unlikedEventDto = eventService.userDontLikeEvent(userId, eventId);

        return ResponseDto.ok(unlikedEventDto);
    }

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
        EventsDto eventsDto = eventService.showEvents(status, process, page-1, size);

        return ResponseDto.ok(eventsDto);
    }

    @GetMapping("{eventId}/info")
    public ResponseDto<?> showDetailInfoOfEvent(@UserId Long userId, @PathVariable Long eventId){
        DetailInfoOfEvent detailInfoOfEvent = eventService.showDetailInfoOfEvent(userId, eventId);

        return ResponseDto.ok(detailInfoOfEvent);
    }

    @GetMapping("{eventId}/brief")
    public ResponseDto<?> showDescriptionOfEvent(@PathVariable Long eventId){
        DetailDescriptionOfEvent detailDescriptionOfEvent = eventService.showDescriptionOfEvent(eventId);

        return ResponseDto.ok(detailDescriptionOfEvent);
    }

    @PostMapping("{eventId}/review")
    public ResponseDto<?> writeReviewOfEvent(
            @UserId Long userId,
            @PathVariable Long eventId,
            @RequestBody WriteReviewOfEvent writeReviewOfEvent
    ){
        eventService.writeReviewOfEvent(userId, eventId, writeReviewOfEvent);

        return ResponseDto.ok(null);
    }

    @GetMapping("{eventId}/top-review")
    public ResponseDto<?> showReviewsOfEvent(@PathVariable Long eventId){
        ReviewsOfEvent reviewsOfEvent = eventService.showReviewsOfEvent(eventId);

        return ResponseDto.ok(reviewsOfEvent);
    }

    @GetMapping("{eventId}/seller")
    public ResponseDto<?> showDetailInfoOfSeller(@PathVariable Long eventId){
        SellerOfEventDto sellerOfEventDto = eventService.showDetailInfoOfSeller(eventId);

        return ResponseDto.ok(sellerOfEventDto);
    }
}
