package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ValidateTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EAssignStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EUserTicketStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.DetailOfTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyTicketsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.PageInfo;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.AuthUtil;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTicketService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final EventImageRepository eventImageRepository;
    private final UserTicketRepository userTicketRepository;
    private final TicketHistoryRepository ticketHistoryRepository;

    /**
     * 사용자 티켓 구매 내역 조회
     */
    public MyTicketsDto showMyTickets(Long userId, Integer page, Integer size){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Page<UserTicket> myTickets = userTicketRepository.findAllByUser(user, PageRequest.of(page, size, Sort.by("boughtAt").descending()));

        List<MyTicketDto> myTicketsDto = myTickets.getContent().stream()
                .map(userTicket -> {
                    String image = eventImageRepository.findByEvent(userTicket.getTicket().getEvent())
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

                    return MyTicketDto.builder()
                            .id(userTicket.getTicket().getId())
                            .name(userTicket.getTicket().getEvent().getName())
                            .image(image)
                            .activatedAt(DateUtil.convertDate(userTicket.getTicket().getActivatedAt()))
                            .boughtAt(DateUtil.convertDate(userTicket.getBoughtAt()))
                            .orderId(userTicket.getOrderId())
                            .build();
                }).toList();


        return MyTicketsDto.builder()
                .pageInfo(PageInfo.convert(myTickets, page))
                .myTickets(myTicketsDto)
                .build();
    }

    /**
     * 사용자 티켓 상세 보기
     */
    public DetailOfTicketDto showDetailOfTicket(Long userId, Long ticketId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TICKET));

        UserTicket userTicket = userTicketRepository.findByUserAndTicket(user, ticket)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER_TICKET));

        String image = eventImageRepository.findByEvent(ticket.getEvent())
                .map(Image::getUrl)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

        Event event = ticket.getEvent();

        return DetailOfTicketDto.builder()
                .ticketId(ticket.getId())
                .image(image)
                .name(event.getName())
                .category(event.getCategory().getCategory())
                .runningTime(event.getRunningTime() + Constants.MINUTE)
                .rating(event.getRating())
                .orderId(userTicket.getOrderId())
                .address(event.getAddress())
                .activateDate(DateUtil.convertDate(ticket.getActivatedAt()))
                .duration(event.getDuration())
                .phoneNumber(event.getUser().getPhoneNumber())
                .buyerId(user.getId())
                .eventHash(event.getEventHash())
                .build();
    }

    /**
     * 사용자 티켓 취소 및 양도하기
     */
    @Transactional
    public void cancelMyTicket(Long userId, Long ticketId) {
        /*
            1. 사용자 소지 티켓 삭제
            2. 티켓 상태 변경 -> TRANSFER
            3. 티켓 이력 추가

         */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TICKET));

        UserTicket userTicket = userTicketRepository.findByUserAndTicket(user, ticket)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER_TICKET));

        // 사용자 소지 티켓 삭제
        userTicketRepository.delete(userTicket);

        // 티켓 상태 변경
        ticket.changeStatus(ETicketStatus.TRANSFER);

        // 티켓 이력 저장
        ticketHistoryRepository.save(
                TicketHistory.builder()
                        .createdAt(LocalDate.now())
                        .status(ETicketStatus.TRANSFER)
                        .ticket(ticket)
                        .build()
        );
    }

    /**
     * 사용자가 참여한 공연 목록
     */
    public ParticipatedEventsDto showParticipatedEvents(Long userId, Integer page, Integer size){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Page<UserTicket> userParticipatedEvents = userTicketRepository.findUserParticipatedEvent(
                user,
                EUserTicketStatus.AFTER_USE,
                PageRequest.of(page, size, Sort.by("boughtAt").descending())
        );

        List<ParticipatedEventDto> participatedEventsDto = userParticipatedEvents.getContent().stream()
                .map(userTicket -> {
                    Event event = userTicket.getTicket().getEvent();

                    String image = eventImageRepository.findByEvent(event)
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));


                    return ParticipatedEventDto.builder()
                            .eventId(event.getId())
                            .image(image)
                            .name(event.getName())
                            .activatedAt(DateUtil.convertDate(userTicket.getTicket().getActivatedAt()))
                            .boughtAt(DateUtil.convertDate(userTicket.getBoughtAt()))
                            .orderId(userTicket.getOrderId())
                            .build();
                }).toList();

        return ParticipatedEventsDto.builder()
                .pageInfo(PageInfo.convert(userParticipatedEvents, page))
                .participatedEventsDto(participatedEventsDto)
                .build();

    }

    /**
     * 티켓 검증하기
     */
    @Transactional
    public void validateTicket(ValidateTicketDto validateTicketDto){
        User user = userRepository.findById(validateTicketDto.userId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Ticket ticket = ticketRepository.findById(validateTicketDto.ticketId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TICKET));

        UserTicket userTicket = userTicketRepository.findByUserAndTicket(user, ticket)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER_TICKET));

        // 사용자 정보와 티켓 정보 불일치 -> 입장 불가
        if (!userTicket.getUser().getDeviceId().equals(validateTicketDto.deviceId())
         || !userTicket.getTicket().getEvent().getEventHash().equals(validateTicketDto.eventHash())) {
            throw new CommonException(ErrorCode.INVALID_TICKET_OWNER);
        }

        // 티켓 사용날짜가 오늘이 아닌 경우 -> 입장 불가
        if (!userTicket.getTicket().getActivatedAt().equals(LocalDate.now())){
            throw new CommonException(ErrorCode.INVALID_TICKET);
        }

        // 이미 입장한 사용권 -> 입장 불가
        if (userTicket.getStatus().equals(EUserTicketStatus.AFTER_USE)){
            throw new CommonException(ErrorCode.ALREADY_USED_TICKET);
        }

        // 사용자 입장시, 참여로 변경
        userTicket.changeStatus(EUserTicketStatus.AFTER_USE);
    }

}
