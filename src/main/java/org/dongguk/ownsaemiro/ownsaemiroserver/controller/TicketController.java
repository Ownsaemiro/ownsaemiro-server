package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.DetailOfTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyTicketsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.UserTicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final UserTicketService userTicketService;

    /**
     * 사용자 티켓 구매 내역 api
     */
    @GetMapping("/purchasing")
    public ResponseDto<?> showMyTickets(
            @UserId Long userId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        MyTicketsDto myTicketsDto = userTicketService.showMyTickets(userId, page-1, size);

        return ResponseDto.ok(myTicketsDto);
    }

    /**
     * 사용자 구매한 티켓 상세 조회
     */
    @GetMapping("/purchasing/{ticketId}")
    public ResponseDto<?> showDetailOfMyTicket(@UserId Long userId, @PathVariable Long ticketId){
        DetailOfTicketDto detailOfTicketDto = userTicketService.showDetailOfTicket(userId, ticketId);

        return ResponseDto.ok(detailOfTicketDto);
    }
}
