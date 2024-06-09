package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.BuyingTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.LikeEventDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.WriteReviewOfEvent;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.UserEventService;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.UserTicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserEventController {
    private final UserEventService userEventService;
    private final UserTicketService userTicketService;

    /**
     * 사용자 행사 좋아요 하기
     */
    @PostMapping("/events/likes")
    public ResponseDto<?> saveUserLikedEvent(@UserId Long userId, @RequestBody LikeEventDto likeEventDto){
        LikedEventDto likedEventDto = userEventService.userLikeEvent(userId, likeEventDto.id());

        return ResponseDto.created(likedEventDto);
    }

    /**
     * 사용자 행사 좋아요 목록 조회하기
     */
    @GetMapping("users/events/likes")
    public ResponseDto<?> showUserLikedEvents(
            @UserId Long userId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        UserLikedEventsDto userLikedEventsDto = userEventService.showUserLikedEvents(userId, page-1, size);

        return ResponseDto.ok(userLikedEventsDto);
    }

    /**
     * 사용자 행사 좋아요 취소하기
     */
    @DeleteMapping("/events/{eventId}/likes")
    public ResponseDto<?> deleteUserLikedEvent(@UserId Long userId, @PathVariable Long eventId){
        UnlikedEventDto unlikedEventDto = userEventService.userDontLikeEvent(userId, eventId);

        return ResponseDto.ok(unlikedEventDto);
    }

    /**
     * 사용자 티켓 구매하기
     */
    @PostMapping("/events/{eventId}")
    public ResponseDto<?> buyingTicket(
            @UserId Long userId,
            @PathVariable Long eventId,
            @RequestBody BuyingTicketDto buyingTicketDto
    ){
        userEventService.buyingTicket(userId, eventId, buyingTicketDto);

        return ResponseDto.ok(null);

    }

    /**
     * 양도 수령하기
     */
    @PatchMapping("/purchasing")
    public ResponseDto<?> purchasingAssignTicket(@UserId Long userId, @RequestBody TicketIdDto ticketIdDto){
        userEventService.purchasingAssignTicket(userId, ticketIdDto);

        return ResponseDto.ok(null);
    }

    /**
     * 사용자 이벤트 리뷰 작성하기
     */
    @PostMapping("/events/{eventId}/review")
    public ResponseDto<?> writeReviewOfEvent(
            @UserId Long userId,
            @PathVariable Long eventId,
            @RequestBody WriteReviewOfEvent writeReviewOfEvent
    ){
        userEventService.writeReviewOfEvent(userId, eventId, writeReviewOfEvent);

        return ResponseDto.ok(null);
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
