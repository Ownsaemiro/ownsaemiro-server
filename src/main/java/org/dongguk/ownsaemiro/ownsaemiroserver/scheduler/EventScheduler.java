package org.dongguk.ownsaemiro.ownsaemiroserver.scheduler;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.service.EventService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventScheduler {
    private final EventService eventService;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateEventStatus(){
        eventService.updateEventState();
    }

}
