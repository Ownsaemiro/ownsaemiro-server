package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Image;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserTicket;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.MyTicketsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.PageInfo;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.EventImageRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserTicketRepository;
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
    private final EventImageRepository eventImageRepository;
    private final UserTicketRepository userTicketRepository;

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

}
