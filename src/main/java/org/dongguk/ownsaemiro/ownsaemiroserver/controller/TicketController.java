package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyTicketsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.UserTicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final UserTicketService userTicketService;

    @GetMapping("/purchasing")
    public ResponseDto<?> showMyTickets(
            @UserId Long userId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        MyTicketsDto myTicketsDto = userTicketService.showMyTickets(userId, page-1, size);

        return ResponseDto.ok(myTicketsDto);
    }
}
