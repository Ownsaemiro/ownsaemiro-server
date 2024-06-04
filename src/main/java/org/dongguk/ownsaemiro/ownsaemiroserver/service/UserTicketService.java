package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.DetailOfTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyTicketsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.PageInfo;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTicketService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final EventImageRepository eventImageRepository;
    private final UserTicketRepository userTicketRepository;

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
                            .activatedAt(DateUtil.convertDate(userTicket.getActivatedAt()))
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

        String orderId = userTicketRepository.findOrderIdByUserAndTicket(user, ticket)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER_TICKET));

        String image = eventImageRepository.findByEvent(ticket.getEvent())
                .map(Image::getUrl)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

        Event event = ticket.getEvent();

        return DetailOfTicketDto.builder()
                .eventId(event.getId())
                .image(image)
                .name(event.getName())
                .category(event.getCategory().getCategory())
                .runningTime(event.getRunningTime() + Constants.MINUTE)
                .rating(event.getRating())
                .address(event.getAddress())
                .duration(event.getDuration())
                .phoneNumber(event.getUser().getPhoneNumber())
                .orderId(orderId)
                .build();
    }
}
