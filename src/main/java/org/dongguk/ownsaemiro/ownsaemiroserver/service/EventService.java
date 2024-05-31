package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.EventRequest;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ApplyEventDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.EventRequestDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.EventRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.EventRequestRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventRequestRepository eventRequestRepository;

    @Transactional
    public Event saveEvent(Long userId, ApplyEventDto applyEventDto){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return eventRepository.save(
                Event.builder()
                        .name(applyEventDto.name())
                        .brief(applyEventDto.description().substring(0, Math.min(applyEventDto.description().length(), 100)))
                        .description(applyEventDto.description())
                        .address(applyEventDto.address())
                        .price(applyEventDto.price())
                        .duration(applyEventDto.startDate() + " ~ " + applyEventDto.endDate())
                        .category(ECategory.toECategory(applyEventDto.category()))
                        .status(EEventStatus.BEFORE)
                        .runningTime((applyEventDto.runningTime() > 180) ? 180 : applyEventDto.runningTime())
                        .user(user)
                        .isApproved(Boolean.FALSE)
                        .build()
        );

    }
    @Transactional
    public EventRequestDto saveEventRequest(Event event){
        EventRequest eventRequest = eventRequestRepository.save(
                EventRequest.builder()
                        .id(event.getId())
                        .event(event)
                        .createdAt(LocalDate.now())
                        .build()
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return EventRequestDto.builder()
                .id(eventRequest.getId())
                .name(eventRequest.getEvent().getName())
                .hostName(event.getUser().getNickname())
                .applyDate(eventRequest.getCreatedAt().format(formatter))
                .duration(event.getDuration())
                .state(eventRequest.getState().getStatus())
                .build();
    }
}
