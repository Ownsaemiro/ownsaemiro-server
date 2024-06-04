package org.dongguk.ownsaemiro.ownsaemiroserver.controller;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.EventService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assignment")
@RequiredArgsConstructor
public class AssignmentController {
    private final EventService eventService;

    @PutMapping("/tickets/{ticketId}")
    public void applyAssignment(@PathVariable Long ticketId){
        eventService.applyAssignment(ticketId);
    }
}
