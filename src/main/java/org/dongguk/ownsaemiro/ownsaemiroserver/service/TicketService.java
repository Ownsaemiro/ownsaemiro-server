package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EAssignStatus;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final FCMService fcmService;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final EventImageRepository eventImageRepository;
    private final NotificationRepository notificationRepository;
    private final UserLikedEventRepository userLikedEventRepository;
    private final UserAssignTicketRepository userAssignTicketRepository;

    /* ================================================================= */
    //                         양도 관련 scheduler                         //
    /* ================================================================= */

    /**
     * 양도 대기 목록에서 랜덤하게 하나씩 뽑아서 양도 받는 사람을 결정
     */
    @Transactional
    public Boolean assignTickets(){
        List<UserAssignTicket> randomUserAssignTicket = userAssignTicketRepository.findRandomUserAssignTicket();
        if (randomUserAssignTicket.size() == 0){
            log.info("양도가 가능한 티켓이 없습니다.");
            return Boolean.FALSE;
        } else {
            log.info("양도가 가능한 티켓이 존재합니다.");
            randomUserAssignTicket.forEach(userAssignTicket -> {
                // 양도 성공 상태로 변경
                userAssignTicket.updateStatus(EAssignStatus.SUCCESS);

                String name = userAssignTicket.getTicket().getEvent().getName();

                // 사용자에게 양도 성공 알림 보내기
                fcmService.sendNotification(
                        userAssignTicket.getUser().getFcmToken(),
                        Constants.ASSIGN_TICKET_COMPLETE_TITLE,
                         name + Constants.ASSIGN_TICKET_COMPLETE_CONTENT
                );

                // 알림 객체 저장하기
                notificationRepository.save(
                        Notification.builder()
                                .user(userAssignTicket.getUser())
                                .title(Constants.ASSIGN_TICKET_COMPLETE_TITLE)
                                .content(name + Constants.ASSIGN_TICKET_COMPLETE_CONTENT)
                                .build()
                );
            });
            log.info("양도 완료");
            return Boolean.TRUE;
        }
    }

    /**
     * 양도에 실패한 userAssignTicket 상태 변경
     */
    @Transactional
    public void failToAssignTicket(){
        userAssignTicketRepository.findFailToAssignTicket()
                .forEach(userAssignTicket -> {
                    // 양도 실패로 상태 변경
                    userAssignTicket.updateStatus(EAssignStatus.FAIL);

                    String name = userAssignTicket.getTicket().getEvent().getName();

                    // 양도 실패에 대한 알림 발송
                    fcmService.sendNotification(
                            userAssignTicket.getUser().getFcmToken(),
                            Constants.ASSIGN_TICKET_FAIL_TITLE,
                            name + Constants.ASSIGN_TICKET_FAIL_CONTENT
                    );

                    // 알림 객체 저장하기
                    notificationRepository.save(
                            Notification.builder()
                                    .user(userAssignTicket.getUser())
                                    .title(Constants.ASSIGN_TICKET_FAIL_TITLE)
                                    .content(name + Constants.ASSIGN_TICKET_FAIL_CONTENT)
                                    .build()
                    );
                });
    }

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
                .eventId(event.getId())
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

        // 같은 티켓에 같은 사용자가 두 번 이상 양도 신청 불가능 -> 예외처리
        if (userAssignTicketRepository.existsByUserAndTicket(user, ticket)){
            throw new CommonException(ErrorCode.INVALID_APPLY_ASSIGN_TWICE);
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
                            .ticketId(userAssignTicket.getTicket().getId())
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
