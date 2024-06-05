package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Image;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Ticket;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AssignTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.AssignTicketsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.PageInfo;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.EventImageRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.TicketRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserTicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventImageRepository eventImageRepository;
    private final UserTicketRepository userTicketRepository;
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

                    String activatedAt = userTicketRepository.findByTicket(ticket)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER_TICKET));

                    return AssignTicketDto.builder()
                            .id(ticket.getId())
                            .image(image)
                            .name(ticket.getEvent().getName())
                            .activatedAt(activatedAt)
                            .build();
                }).toList();


        return AssignTicketsDto.builder()
                .pageInfo(PageInfo.convert(assignTickets, page))
                .assignTicketsDto(assignTicketsDto)
                .build();
    }
}
