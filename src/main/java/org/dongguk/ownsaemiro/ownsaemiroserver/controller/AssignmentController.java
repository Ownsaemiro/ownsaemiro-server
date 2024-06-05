package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.annotation.UserId;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ResponseDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AllAboutEventDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AssignTicketsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.EventService;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.TicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {
    private final EventService eventService;
    private final TicketService ticketService;

    /**
     *  양도 티켓 목록 api
     */
    @GetMapping("/tickets")
    public ResponseDto<?> showAssignTickets(
            @RequestParam("filter") String category,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        AssignTicketsDto assignTicketsDto = ticketService.showAssignTickets(category, page - 1, size);

        return ResponseDto.ok(assignTicketsDto);
    }

    /**
     *  양도 티켓 상세보기 api
     */
    @GetMapping("/tickets/{ticketId}")
    public ResponseDto<?> showDetailOfAssignTicket(@UserId Long userId, @PathVariable Long ticketId){
        AllAboutEventDto allAboutEventDto = ticketService.showDetailOfAssignTicket(userId, ticketId);

        return ResponseDto.ok(allAboutEventDto);
    }

    /**
     * 양도 신청하기 api
     */
    @PutMapping("/tickets/{ticketId}")
    public ResponseDto<?> applyAssignment(@UserId Long userId, @PathVariable Long ticketId){
        ticketService.applyAssignment(userId, ticketId);

        return ResponseDto.created(null);
    }
}
