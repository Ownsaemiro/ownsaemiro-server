package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Image;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Ticket;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AllAboutEventDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AssignTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AssignTicketsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.PageInfo;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final EventImageRepository eventImageRepository;
    private final UserLikedEventRepository userLikedEventRepository;

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

        Event event = ticket.getEvent();

        String image = eventImageRepository.findByEvent(event)
                .map(Image::getUrl)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));


        return AllAboutEventDto.builder()
                .id(event.getId())
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
}
