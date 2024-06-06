package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.EventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    /**
     * 행사 검색하기
     */
    @GetMapping("/search")
    public ResponseDto<?> searchEvents(
            @RequestParam("name") String name,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        SearchEventsDto searchEventsDto = eventService.searchEvent(name, page-1, size);

        return ResponseDto.ok(searchEventsDto);
    }

    /**
     * 행사 목록 보여주기
     */
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

    /**
     * 행사 정보 상세 보기
     */

    @GetMapping("{eventId}/info")
    public ResponseDto<?> showDetailInfoOfEvent(@UserId Long userId, @PathVariable Long eventId){
        DetailInfoOfEventDto detailInfoOfEventDto = eventService.showDetailInfoOfEvent(userId, eventId);

        return ResponseDto.ok(detailInfoOfEventDto);
    }

    @GetMapping("{eventId}/brief")
    public ResponseDto<?> showDescriptionOfEvent(@PathVariable Long eventId){
        DetailDescriptionOfEventDto detailDescriptionOfEventDto = eventService.showDescriptionOfEvent(eventId);

        return ResponseDto.ok(detailDescriptionOfEventDto);
    }

    @GetMapping("{eventId}/top-review")
    public ResponseDto<?> showReviewsOfEvent(@PathVariable Long eventId){
        ReviewsOfEventDto reviewsOfEventDto = eventService.showReviewsOfEvent(eventId);

        return ResponseDto.ok(reviewsOfEventDto);
    }

    @GetMapping("{eventId}/seller")
    public ResponseDto<?> showDetailInfoOfSeller(@PathVariable Long eventId){
        SellerOfEventDto sellerOfEventDto = eventService.showDetailInfoOfSeller(eventId);

        return ResponseDto.ok(sellerOfEventDto);
    }

    /**
     * 행사에 대한 리뷰 목록 조회 api
     */
    @GetMapping("/{eventId}/reviews")
    public ResponseDto<?> showReviewsOfEvent(
            @PathVariable Long eventId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        AllReviewsOfEventDto allReviewsOfEventDto = eventService.showAllReviewsOfEvent(eventId, page - 1, size);

        return ResponseDto.ok(allReviewsOfEventDto);
    }

    /**
     * 인기 있는 행사
     */
    @GetMapping("/popular")
    public ResponseDto<?> showPopularEvents(){
        return ResponseDto.ok(eventService.showPopularEvents());
    }

    /**
     * 사용자 추천 행사
     */
    @GetMapping("/recommends")
    public ResponseDto<?> showRecommendEvents() {
        return ResponseDto.ok(eventService.showRecommendEvents());
    }
}
