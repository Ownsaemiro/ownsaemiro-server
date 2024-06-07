package org.dongguk.ownsaemiro.ownsaemiroserver.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.TicketService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class AssignScheduler {
    private final TicketService ticketService;
    @Scheduled(cron = "0 0 * * * *")
    public void assignTickets(){
        if (ticketService.assignTickets()){
            ticketService.failToAssignTicket();
            log.info("상태 변경 완료");
        }
    }
}
