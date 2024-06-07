package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.EventRequest;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Ticket;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ChangeEventRequestStatusDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.blockchain.BlockChainResponse;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventRequestStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.EventRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.EventRequestRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.TicketRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.DateUtil;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.RestClientUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final RestClientUtil restClientUtil;
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final EventRequestRepository eventRequestRepository;

    /* ================================================================= */
    //                          관리자 api                                 //
    /* ================================================================= */

    /**
     * 관리자가 보는 판매자들의 판매 요청 목록
     */
    public AdminApplyEventDto searchOrShowAppliesOfSeller(String name, String strState, Integer page, Integer size){
        Page<EventRequest> appliesOfSeller;
        if (name == null && strState == null){
            // 일반 목록 조회
            appliesOfSeller = eventRequestRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        } else if (name == null && strState != null) {
            if (strState.equals(Constants.ALL)){
                // 전체 조건인 경우
                appliesOfSeller = eventRequestRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
            } else {
                // 특정 조건 검색인 경우
                appliesOfSeller = eventRequestRepository.findAllAppliesByStatus(
                        EEventRequestStatus.toEnum(strState),
                        PageRequest.of(page, size, Sort.by("createdAt").descending())
                );
            }
        } else if (name != null && strState == null){
            // 이름으로 조회
            appliesOfSeller = eventRequestRepository.findAllAppliesByName(
                    name,
                    PageRequest.of(page, size, Sort.by("createdAt").descending())
            );
        } else {
            if (strState.equals(Constants.ALL)){
                // 이름 + 전체 조건인 경우
                appliesOfSeller = eventRequestRepository.findAllAppliesByName(
                        name,
                        PageRequest.of(page, size, Sort.by("createdAt").descending())
                );
            } else {
                // 이름 + 특정 조건 검색인 경우
                appliesOfSeller = eventRequestRepository.findAllAppliesByNameAndState(
                        name,
                        EEventRequestStatus.toEnum(strState),
                        PageRequest.of(page, size, Sort.by("createdAt").descending())
                );
            }
        }

        return AdminApplyEventDto.builder()
                .pageInfo(PageInfo.convert(appliesOfSeller, page))
                .eventRequestsDto(extract(appliesOfSeller))
                .build();
    }

    /**
     *  관리자가 보는 판매자들의 판매 요청 상세
     */
    public DetailOfEventRequestDto showDetailOfEventRequest(Long eventRequestId){
        EventRequest eventRequest = eventRequestRepository.findById(eventRequestId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT_REQUEST));

        return DetailOfEventRequestDto.builder()
                .name(eventRequest.getEvent().getName())
                .duration(eventRequest.getEvent().getDuration())
                .address(eventRequest.getEvent().getAddress())
                .hostNickname(eventRequest.getUser().getNickname())
                .runningTime(eventRequest.getEvent().getRunningTime())
                .description(eventRequest.getEvent().getDescription())
                .build();
    }

    /**
     * 관리자 행사 승인 여부 결정
     */
    @Transactional
    public ChangedEventRequestStatusDto changeEventRequestState(ChangeEventRequestStatusDto changeEventRequestStatusDto){
        EventRequest eventRequest = eventRequestRepository.findById(changeEventRequestStatusDto.id())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT_REQUEST));

        EEventRequestStatus eEventRequestStatus = EEventRequestStatus.toEnum(changeEventRequestStatusDto.status());

        // 잘못된 인자 값이 들어온 경우
        if (eEventRequestStatus == null)
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);

        // 승인완료시, event에도 approved 적용
        if (eEventRequestStatus.equals(EEventRequestStatus.COMPLETE)) {
            Event event = eventRepository.findById(changeEventRequestStatusDto.id())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));
            // 이벤트 승인완료 처리
            event.changeApproved(Boolean.TRUE);
            event.changeStatus(EEventStatus.SELLING);

            // TODO: 스포츠와 콘서트의 경우에는 예측 모델 통과하기

            // 이벤트 관련 블록체인 정보 업데이트
            BlockChainResponse response = restClientUtil.sendRequestToPublishTickets(eventRequest.getSeat());
            if (!response.getSuccess()){
                throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
            } else {
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

                event.updateBlockChainInfo(response.getData().getEventHash(), response.getData().getContractAddress());
            }

        }

        EEventRequestStatus status = eventRequest.updateStatus(eEventRequestStatus);

        return ChangedEventRequestStatusDto.builder()
                .id(eventRequest.getId())
                .status(status.getStatus())
                .build();
    }

    /**
     * 페이지네이션 추출 함수
     */
    private static List<EventRequestDto> extract(Page<EventRequest> mySearchResult) {

        List<EventRequestDto> eventRequestsDto = mySearchResult.getContent().stream()
                .map(eventRequest -> EventRequestDto.builder()
                        .id(eventRequest.getId())
                        .name(eventRequest.getEvent().getName())
                        .hostName(eventRequest.getUser().getNickname())
                        .applyDate(DateUtil.convertDate(eventRequest.getCreatedAt()))
                        .duration(eventRequest.getEvent().getDuration())
                        .state(eventRequest.getState().getStatus())
                        .build()
                ).toList();
        return eventRequestsDto;
    }
}
