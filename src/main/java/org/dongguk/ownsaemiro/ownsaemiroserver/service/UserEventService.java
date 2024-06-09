package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.BuyingTicketDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.WriteReviewOfEvent;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.blockchain.BlockChainResponse;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EAssignStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.AuthUtil;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.RestClientUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventService {
    private final RestClientUtil restClientUtil;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserWalletRepository userWalletRepository;
    private final UserTicketRepository userTicketRepository;
    private final EventImageRepository eventImageRepository;
    private final EventReviewRepository eventReviewRepository;
    private final TicketHistoryRepository ticketHistoryRepository;
    private final UserLikedEventRepository userLikedEventRepository;
    private final UserAssignTicketRepository userAssignTicketRepository;
    private final UserWalletHistoryRepository userWalletHistoryRepository;


    /* ================================================================= */
    //                           사용자 행사 api                            //
    /* ================================================================= */

    /**
     * 사용자 행사 좋아요
     */
    @Transactional
    public LikedEventDto userLikeEvent(Long userId, Long eventId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Event event = eventRepository.findByIdAndIsApproved(eventId, Boolean.TRUE)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        // 이미 사용자가 좋아요한 행사인 경우
        if (userLikedEventRepository.existsByUserAndEvent(user, event)){
            throw new CommonException(ErrorCode.ALREADY_LIKED_EVENT);
        }

        // 사용자 좋아요 생성
        UserLikedEvent userLikedEvent = userLikedEventRepository.save(
                UserLikedEvent.create(user, event)
        );

        return LikedEventDto.builder()
                .likedId(userLikedEvent.getId())
                .eventId(event.getId())
                .isLiked(Boolean.TRUE)
                .build();
    }

    /**
     * 사용자 행사 좋아요 목록
     */
    public UserLikedEventsDto showUserLikedEvents(Long userId, Integer page, Integer size){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Page<UserLikedEvent> userLikedEvents = userLikedEventRepository.findAllByUser(user, PageRequest.of(page, size));

        List<UserLikedEventDto> userLikedEventsDto = userLikedEvents.getContent().stream()
                .map(userLikedEvent -> {
                    String imageUrl = eventImageRepository.findByEvent(userLikedEvent.getEvent())
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

                    return UserLikedEventDto.builder()
                            .eventId(userLikedEvent.getEvent().getId())
                            .name(userLikedEvent.getEvent().getName())
                            .url(imageUrl)
                            .duration(userLikedEvent.getEvent().getDuration())
                            .isLiked(Boolean.TRUE)
                            .build();
                }).toList();

        return UserLikedEventsDto.builder()
                .pageInfo(PageInfo.convert(userLikedEvents, page))
                .userLikedEventsDto(userLikedEventsDto)
                .build();
    }

    /**
     * 사용자 행사 좋아요 취소하기
     */
    @Transactional
    public UnlikedEventDto userDontLikeEvent(Long userId, Long eventId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Event event = eventRepository.findByIdAndIsApproved(eventId, Boolean.TRUE)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        // 이미 사용자가 좋아요한 행사인 경우
        UserLikedEvent userLikedEvent = userLikedEventRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_LIKED));

        // 사용자 좋아요 삭제
        userLikedEventRepository.delete(userLikedEvent);

        return UnlikedEventDto.builder()
                .id(eventId)
                .isLiked(Boolean.FALSE)
                .build();
    }

    /**
     * 사용자 행사 티켓 구매
     */
    @Transactional
    public void buyingTicket(Long userId, Long eventId, BuyingTicketDto buyingTicketDto){
        /*
            1. 사용자 지갑에서 잔액 확인 및 대소 비교
            2. 구매 전인 티켓이 남아있는지 확인
            3. user ticket 객체 생성, 포인트 차감
            4. ticket 상태 변경
            5. user, ticket history 생성
         */

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        // 승인되지 않은 행사라면 -> 예외처리
        if (!event.getIsApproved()){
            throw new CommonException(ErrorCode.NOT_FOUND_EVENT);
        }

        UserWallet userWallet = userWalletRepository.findById(user.getId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_WALLET));

        // 사용자 지갑 잔액 확인, 행사 티켓 가격과 대소 비교
        if (userWallet.getPoint() < event.getPrice())
            throw new CommonException(ErrorCode.NOT_ENOUGH_POINT);

        // 구매 가능한 티켓 확인, 없다면 행사 매진처리 및 예외처리
        if (!ticketRepository.existsByEventAndStatus(event, ETicketStatus.BEFORE)){
             Integer alreadyPublished = Math.toIntExact(ticketRepository.countByEvent(event));
             // 추가 발행이 가능한 경우
             if (alreadyPublished < event.getSeat()){
                 BlockChainResponse response = restClientUtil.sendRequestToAdditionalTickets(
                         event.getContractAddress(),
                         event.getSeat(),
                         alreadyPublished
                 );

                 // 블록체인 서버와 통신이 실패한 경우
                 if (!response.getSuccess()){
                     throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);
                 } else {
                     // 블록체인 서버와 통신 성공, 티켓 추가 생성
                     response.getData().getTickets().forEach(
                             blockChainTicket -> {
                                 ticketRepository.save(
                                         Ticket.builder()
                                                 .event(event)
                                                 .ticketNumber(blockChainTicket.getTicketNumber())
                                                 .status(ETicketStatus.BEFORE)
                                                 .build()
                                 );
                             }
                     );
                 }
             }
             // 더 이상 추가 티켓 발행이 안되는 경우
             else {
                 throw new CommonException(ErrorCode.SOLDOUT_EVENT);
             }
        }

        // 구매 가능한 티켓 조회
        Ticket ticket = ticketRepository.findFirstByEventAndStatus(event, ETicketStatus.BEFORE)
                .orElseThrow(() -> new CommonException(ErrorCode.SOLDOUT_EVENT));

        // 사용자 구매 티켓 저장
        userTicketRepository.save(
                UserTicket.builder()
                        .user(user)
                        .ticket(ticket)
                        .boughtAt(LocalDate.now())
                        .orderId(AuthUtil.makeOrderId())
                        .build()
        );

        // 사용자 지갑 포인트 차감
        userWallet.pay(event.getPrice());

        // 티켓의 상태 변경 and 티켓 입장 날짜 지정
        ticket.changeStatus(ETicketStatus.OCCUPIED);
        ticket.chooseActivateDate(buyingTicketDto.buyingDate());

        // 사용자 지갑 이력 저장
        userWalletHistoryRepository.save(
                UserWalletHistory.builder()
                        .amount(-1 * event.getPrice())
                        .userWallet(userWallet)
                        .createdAt(LocalDate.now())
                        .build()
        );

        // 티켓 구매 이력 저장
        ticketHistoryRepository.save(
                TicketHistory.builder()
                        .ticket(ticket)
                        .status(ETicketStatus.OCCUPIED)
                        .createdAt(LocalDate.now())
                        .build()
        );

    }

    /**
     * 양도 신청 성공한 티켓 사용자가 구매하기
     */
    @Transactional
    public void purchasingAssignTicket(Long userId, TicketIdDto ticketIdDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Ticket ticket = ticketRepository.findById(ticketIdDto.ticketId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TICKET));

        UserAssignTicket userAssignTicket = userAssignTicketRepository.findByUserAndTicket(user, ticket)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_ASSIGN_TICKET));

        // 당첨되지 않은 티켓 -> 예외 처리
        if (!userAssignTicket.getStatus().equals(EAssignStatus.SUCCESS)) {
            throw new CommonException(ErrorCode.INVALID_ASSIGN_TICKET);
        }

        UserWallet userWallet = userWalletRepository.findById(user.getId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_WALLET));

        // 행사 객체 가져오기
        Event event = ticket.getEvent();

        // 사용자 포인트 충분한지 확인 -> 예외처리
        if (userWallet.getPoint() < event.getPrice()){
            throw new CommonException(ErrorCode.NOT_ENOUGH_POINT);
        }

        // 사용자 구매 티켓 저장
        userTicketRepository.save(
                UserTicket.builder()
                        .user(user)
                        .ticket(ticket)
                        .boughtAt(LocalDate.now())
                        .orderId(AuthUtil.makeOrderId())
                        .build()
        );

        // 사용자 지갑 포인트 차감
        userWallet.pay(event.getPrice());

        // 티켓의 상태 변경 and 티켓 입장 날짜 지정
        ticket.changeStatus(ETicketStatus.OCCUPIED);

        // 사용자 지갑 이력 저장
        userWalletHistoryRepository.save(
                UserWalletHistory.builder()
                        .amount(-1 * event.getPrice())
                        .userWallet(userWallet)
                        .createdAt(LocalDate.now())
                        .build()
        );

        // 티켓 구매 이력 저장
        ticketHistoryRepository.save(
                TicketHistory.builder()
                        .ticket(ticket)
                        .status(ETicketStatus.OCCUPIED)
                        .createdAt(LocalDate.now())
                        .build()
        );
    }

    /**
     * 행사 리뷰 작성
     */
    @Transactional
    public void writeReviewOfEvent(Long userId, Long eventId, WriteReviewOfEvent writeReviewOfEvent){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        eventReviewRepository.save(
                EventReview.create(user, event, writeReviewOfEvent)
        );
    }

}
