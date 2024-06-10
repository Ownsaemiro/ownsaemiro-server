package org.dongguk.ownsaemiro.ownsaemiroserver.scheduler;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.EventService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EventScheduler {
    private final EventService eventService;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateEventStatus(){
        eventService.updateEventState();
    }

}
