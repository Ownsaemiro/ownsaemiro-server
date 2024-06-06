package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static ch.qos.logback.classic.spi.ThrowableProxyVO.build;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final EventImageRepository eventImageRepository;
    private final UserLikedEventRepository userLikedEventRepository;
    private final UserAssignTicketRepository userAssignTicketRepository;

    /* ================================================================= */
    //                           사용자 양도 api                            //
    /* ================================================================= */

    /**
     * 양도 티켓 목록 조회
     */
    public AssignTicketsDto showAssignTickets(String strCategory, Integer page, Integer size){
        ECategory category = ECategory.filterCondition(strCategory);

        Page<Ticket> assignTickets;
        if (Constants.ALL.equals(strCategory)){
            // 카테고리 조건 없이 전체 검색
            assignTickets = ticketRepository.findAssignTickets(PageRequest.of(page, size));
        } else if (category != null){
            // 카테고리 포함 검색
            assignTickets = ticketRepository.findAssignTicketsByCategory(category, PageRequest.of(page, size));
        } else {
            // 잘못된 인자값
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        List<AssignTicketDto> assignTicketsDto = assignTickets.getContent().stream()
                .map(ticket -> {
                    String image = eventImageRepository.findByEvent(ticket.getEvent())
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

                    return AssignTicketDto.builder()
                            .id(ticket.getId())
                            .image(image)
                            .name(ticket.getEvent().getName())
                            .activatedAt(DateUtil.convertDate(ticket.getActivatedAt()))
                            .build();
                }).toList();


        return AssignTicketsDto.builder()
                .pageInfo(PageInfo.convert(assignTickets, page))
                .assignTicketsDto(assignTicketsDto)
                .build();
    }

    /**
     * 양도 티켓 상세 조회
     */
    public AllAboutEventDto showDetailOfAssignTicket(Long userId, Long ticketId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TICKET));

        // 양도 티켓이 아닌 경우 -> 예외 발생
        if (!ticket.getStatus().equals(ETicketStatus.TRANSFER)){
            throw new CommonException(ErrorCode.INVALID_ASSIGN_TICKET);
        }

        Event event = ticket.getEvent();

        String image = eventImageRepository.findByEvent(event)
                .map(Image::getUrl)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));


        return AllAboutEventDto.builder()
                .id(ticket.getId())
                .image(image)
                .name(event.getName())
                .runningTime(event.getRunningTime() + Constants.MINUTE)
                .category(event.getCategory().getCategory())
                .rating(event.getRating())
                .address(event.getAddress())
                .activatedAt(DateUtil.convertDate(ticket.getActivatedAt()))
                .phoneNumber(event.getUser().getPhoneNumber())
                .description(event.getDescription())
                .isLiked(userLikedEventRepository.existsByUserAndEvent(user, event))
                .build();
    }

    /**
     * 티켓 양도 신청하기
     */
    @Transactional
    public void applyAssignment(Long userId, Long ticketId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TICKET));

        // 양도 티켓이 아닌 경우, -> 예외 처리
        if (!ticket.getStatus().equals(ETicketStatus.TRANSFER)){
            throw new CommonException(ErrorCode.INVALID_ASSIGN_TICKET);
        }

        userAssignTicketRepository.save(
                UserAssignTicket.builder()
                        .user(user)
                        .ticket(ticket)
                        .createdAt(LocalDate.now())
                        .build()
        );
    }

    /**
     * 사용자 티켓 양도 목록 확인하기
     */
    public MyTicketsWaitingDto showMyTicketWaiting(Long userId, Integer page, Integer size){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Page<UserAssignTicket> userWaiting = userAssignTicketRepository.findAllByUser(
                user,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        List<MyTicketWaitingDto> myTicketsWaiting = userWaiting.getContent().stream()
                .map(userAssignTicket -> {
                    Event event = userAssignTicket.getTicket().getEvent();

                    String image = eventImageRepository.findByEvent(event)
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

                    return MyTicketWaitingDto.builder()
                            .name(event.getName())
                            .image(image)
                            .status(userAssignTicket.getStatus().getStatus())
                            .activatedAt(DateUtil.convertDate(userAssignTicket.getTicket().getActivatedAt()))
                            .build();
                }).toList();

        return MyTicketsWaitingDto.builder()
                .pageInfo(PageInfo.convert(userWaiting,page))
                .myTicketsWaiting(myTicketsWaiting)
                .build();
    }
}
