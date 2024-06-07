package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.EventImage;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.EventRequest;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ApplyEventDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ChangeSellingEventStatusDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventRequestStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.EventImageRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.EventRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.EventRequestRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerEventService {
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventImageRepository eventImageRepository;
    private final EventRequestRepository eventRequestRepository;

    /* ================================================================= */
    //                          판매자 api                                 //
    /* ================================================================= */

    /**
     * 판매자 판매 이력 목록 조회 + 검색 조건
     */
    public MyEventHistoriesDto searchOrShowMyEvents(Long userId, String name, String strStatus, Integer page, Integer size){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        Page<EventRepository.EventHistory> myApprovedHistories;
        if (name == null && strStatus == null){
            // 검색 조건이 없는 일반적인 목록 조회인 경우
            myApprovedHistories = eventRepository.findAllMyApprovedHistories(
                    user,
                    PageRequest.of(page, size, Sort.by("createdAt").descending())
            );

        } else if (name == null && strStatus != null){
            if (strStatus.equals(Constants.ALL)){
                // 모든 상태에 대해서 조회인 경우
                myApprovedHistories = eventRepository.findAllMyApprovedHistories(
                        user,
                        PageRequest.of(page, size, Sort.by("createdAt").descending())
                );
            } else {
                // 특정 상태에 대한 조회인 경우
                EEventStatus eEventStatus = EEventStatus.toEnum(strStatus);
                myApprovedHistories = eventRepository.findAllMyApprovedHistoriesByStatus(
                        user,
                        eEventStatus,
                        PageRequest.of(page, size, Sort.by("createdAt").descending())
                );
            }

        } else if (name != null && strStatus == null){
            // 판매 이력 검색어만 넘어온 경우
            myApprovedHistories = eventRepository.findAllMyApprovedHistoriesByName(
                    user,
                    name,
                    PageRequest.of(page, size, Sort.by("createdAt").descending())
            );

        } else {
            // 검색어와 strStatus 모두 넘어온 경우
            if (strStatus.equals(Constants.ALL)){
                // 모든 상태 + 검색어에 대해서 조회인 경우
                myApprovedHistories = eventRepository.findAllMyApprovedHistoriesByName(
                        user,
                        name,
                        PageRequest.of(page, size, Sort.by("createdAt").descending())
                );
            } else {
                // 특정 상태 + 검색어에 대한 조회인 경우
                EEventStatus eEventStatus = EEventStatus.toEnum(strStatus);
                myApprovedHistories = eventRepository.findAllMyApprovedHistoriesByNameAndStatus(
                        user,
                        name,
                        eEventStatus,
                        PageRequest.of(page, size, Sort.by("createdAt").descending())
                );
            }
        }

        List<EventHistoryDto> eventHistoriesDto = extracted(myApprovedHistories);


        return MyEventHistoriesDto.builder()
                .pageInfo(PageInfo.convert(myApprovedHistories, page))
                .eventHistoriesDto(eventHistoriesDto)
                .build();
    }

    private static List<EventHistoryDto> extracted(Page<EventRepository.EventHistory> myEvents) {
        return myEvents.getContent().stream()
                .map(eventHistory -> EventHistoryDto.builder()
                        .id(eventHistory.getEvent().getId())
                        .name(eventHistory.getEvent().getName())
                        .seat(eventHistory.getEvent().getSeat())
                        .applyDate(DateUtil.convertDate(eventHistory.getCreatedAt()))
                        .hostNickname(eventHistory.getEvent().getUser().getNickname())
                        .status(eventHistory.getEvent().getStatus().getState())
                        .duration(eventHistory.getEvent().getDuration())
                        .build()
                ).toList();
    }


    /**
     * 판매자 판매 행사 상태 변경
     */
    @Transactional
    public ChangeEventStatusDto changeEventStatus(Long userId, ChangeSellingEventStatusDto changeSellingEventStatusDto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Event event = eventRepository.findById(changeSellingEventStatusDto.id())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        // 이벤트를 신청한 동일한 유저가 아니라면 -> exception
        if (!event.getUser().equals(user))
            throw new CommonException(ErrorCode.FORBIDDEN_ROLE);

        EEventStatus changedStatus = event.changeStatus(EEventStatus.toEnum(changeSellingEventStatusDto.status()));

        return ChangeEventStatusDto.builder()
                .id(event.getId())
                .status(changedStatus.getState())
                .build();

    }

    /*  판매자 판매 요청 관련 로직  */
    /**
     * 이벤트 생성
     */
    @Transactional
    public Event saveEvent(Long userId, MultipartFile image, ApplyEventDto applyEventDto) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 이벤트 저장
        Event event = eventRepository.save(
                Event.builder()
                        .name(applyEventDto.name())
                        .description(applyEventDto.description())
                        .address(applyEventDto.address())
                        .price(applyEventDto.price())
                        .rating(applyEventDto.rating())
                        .duration(applyEventDto.startDate() + Constants.STR_CONNECTOR + applyEventDto.endDate())
                        .seat(applyEventDto.seats())
                        .category(ECategory.toECategory(applyEventDto.category()))
                        .status(EEventStatus.BEFORE)
                        .runningTime((applyEventDto.runningTime() > 180) ? 180 : applyEventDto.runningTime())
                        .user(user)
                        .isApproved(Boolean.FALSE)
                        .build()
        );

        // 행사 이미지를 저장
        if (!image.isEmpty() && s3Service.readyForUpload(event, image)){
            String url = s3Service.uploadToS3(event, image);
            eventImageRepository.save(
                    EventImage.builder()
                            .url(url)
                            .name(image.getOriginalFilename().split("\\.")[0] + user.getSerialId())
                            .createdAt(LocalDate.now())
                            .event(event)
                            .build()
            );
        }

        return event;
    }

    /**
     * 이벤트 요청 생성
     */
    @Transactional
    public EventRequestDto saveEventRequest(Event event){
        EventRequest eventRequest = eventRequestRepository.save(
                EventRequest.builder()
                        .id(event.getId())
                        .event(event)
                        .user(event.getUser())
                        .seat((event.getSeat()))
                        .createdAt(LocalDate.now())
                        .build()
        );

        return EventRequestDto.builder()
                .id(eventRequest.getId())
                .name(eventRequest.getEvent().getName())
                .hostName(event.getUser().getNickname())
                .applyDate(DateUtil.convertDate(eventRequest.getCreatedAt()))
                .duration(event.getDuration())
                .state(eventRequest.getState().getStatus())
                .build();
    }

    /**
     * 나의 행사 신청 목록 확인하기
     */
    public MyAppliesDto showMyApplies(Long userId, Integer page, Integer size){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Page<EventRequest> myApplies = eventRequestRepository.findAllByUser(
                user,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        List<EventRequestDto> eventRequestsDto = getEventRequestsDto(myApplies);

        return MyAppliesDto.builder()
                .pageInfo(PageInfo.convert(myApplies, page))
                .eventApplies(eventRequestsDto)
                .build();
    }

    /**
     * 나의 행사 신청 목록 검색하기
     */

    public MyAppliesDto searchMyApplies(Long userId, String search, String state, Integer page, Integer size){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<EventRequest> mySearchResult;

        EEventRequestStatus status = EEventRequestStatus.toEnum(state);
        if (status != null) {
            // 검색 상태 조건이 전체인 경우
            if (state.equals(Constants.ALL))
                mySearchResult = eventRequestRepository.searchAllByUserAndState(user, status, search, pageable);
                // 검색 상태 조건이 올바르지 않은 경우
            else
                throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        } else {
            // 검색 상태 조건이 승인대기, 승인거절, 승인완료 인 경우
            mySearchResult = eventRequestRepository.searchAllByUser(user, search, pageable);
        }

        List<EventRequestDto> eventRequestsDto = getEventRequestsDto(mySearchResult);

        return MyAppliesDto.builder()
                .pageInfo(PageInfo.convert(mySearchResult, page))
                .eventApplies(eventRequestsDto)
                .build();
    }

    /**
     * 이벤트 요청 삭제 (이벤트도 삭제: cascade)
     */
    @Transactional
    public void cancelApply(Long userId, Long eventRequestId){
        EventRequest eventRequest = eventRequestRepository.findById(eventRequestId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT_REQUEST));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        if (!eventRequest.getEvent().getUser().equals(user))
            throw new CommonException(ErrorCode.INVALID_USER);

        eventRequestRepository.delete(eventRequest);
    }

    /**
     * 페이지네이션 결과 dto로 변환하는 함수
     */
    private static List<EventRequestDto> getEventRequestsDto(Page<EventRequest> mySearchResult) {

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
