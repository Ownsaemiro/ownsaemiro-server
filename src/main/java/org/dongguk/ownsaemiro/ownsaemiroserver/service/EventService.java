package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ApplyEventDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ChangeEventRequestStatusDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.ChangeSellingEventStatusDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.WriteReviewOfEvent;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventRequestStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.*;
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
public class EventService {
    private final UserImageRepository userImageRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventImageRepository eventImageRepository;
    private final EventReviewRepository eventReviewRepository;
    private final EventRequestRepository eventRequestRepository;
    private final UserLikedEventRepository userLikedEventRepository;

    /* ================================================================= */
    //                          사용자 api                                 //
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
                            .likedId(userLikedEvent.getId())
                            .eventId(userLikedEvent.getEvent().getId())
                            .name(userLikedEvent.getEvent().getName())
                            .url(imageUrl)
                            .duration(userLikedEvent.getEvent().getDuration())
                            .build();
                }).toList();

        return UserLikedEventsDto.builder()
                .pageInfo(PageInfo.convert(userLikedEvents, page))
                .userLikedEventsDto(userLikedEventsDto)
                .build();
    }

    /**
     * 사용자 행사 좋아요 취소
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
     * 사용자 행사 목록 조회
     */
    public EventsDto showEvents(String strStatus, String strCategory, Integer page, Integer size){
        ECategory category = ECategory.filterCondition(strCategory);
        EEventStatus status = EEventStatus.toEnum(strStatus);

        Page<Event> events;
        if (Constants.ALL.equals(strCategory)) {
            // 전체 검색
            events = eventRepository.findAllByStatus(status, PageRequest.of(page, size));
        } else if (category == null) {
            // 카테고리별 검색
            events = eventRepository.findAllByStatusAndCategory(status, category, PageRequest.of(page, size));
        } else {
            // 잘못된 인자값
            throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
        }

        List<EventDto> eventsDto = events.getContent().stream()
                .map(event -> {
                    String imageUrl = eventImageRepository.findByEvent(event)
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

                    return EventDto.builder()
                            .id(event.getId())
                            .image(imageUrl)
                            .name(event.getName())
                            .address(event.getAddress())
                            .duration(event.getDuration())
                            .build();
                }
                ).toList();

        return EventsDto.builder()
                .pageInfo(PageInfo.convert(events, page))
                .eventsDto(eventsDto)
                .build();
    }

    /**
     * 행사 상세 정보 보기 - info
     */
    public DetailInfoOfEventDto showDetailInfoOfEvent(Long userId, Long eventId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        String image = eventImageRepository.findByEvent(event)
                .map(Image::getUrl)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

        return DetailInfoOfEventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .url(image)
                .category(event.getCategory().getCategory())
                .runningTime(event.getRunningTime() + Constants.MINUTE)
                .rating(event.getRating())
                .address(event.getAddress())
                .phoneNumber(event.getUser().getPhoneNumber())
                .price(event.getPrice())
                .duration(event.getDuration())
                .isLiked(userLikedEventRepository.existsByUserAndEvent(user, event))
                .build();
    }

    /**
     * 행사 상세 정보 보기 - description
     */
    public DetailDescriptionOfEventDto showDescriptionOfEvent(Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        return DetailDescriptionOfEventDto.builder()
                .description(event.getDescription())
                .build();
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

    /**
     * 행사 상세 정보 보기 - review
     */
    public ReviewsOfEventDto showReviewsOfEvent(Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        List<ReviewOfEventDto> reviewOfEventDtos = eventReviewRepository.findTop3ByEvent(event).stream()
                .map(eventReview -> {
                    String image = userImageRepository.findByUser(eventReview.getUser())
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

                    return ReviewOfEventDto.builder()
                            .id(eventReview.getId())
                            .image(image)
                            .nickname(eventReview.getUser().getNickname())
                            .content(eventReview.getContent())
                            .build();
                }).toList();

        return ReviewsOfEventDto.builder()
                .reviewsDto(reviewOfEventDtos)
                .build();
    }

    /**
     * 행사 상세 정보 보기 - seller
     */
    public SellerOfEventDto showDetailInfoOfSeller(Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        return SellerOfEventDto.builder()
                .nickname(event.getUser().getNickname())
                .runningTime(event.getRunningTime() + Constants.MINUTE)
                .rating(event.getRating())
                .address(event.getAddress())
                .build();
    }

    /**
     * 사용자 행사 검색
     */
    public SearchEventsDto searchEvent(String name, Integer page, Integer size){
        Page<Event> events = eventRepository.searchAllByName(name, PageRequest.of(page, size));

        List<SearchEventDto> searchEventsDto = events.getContent().stream()
                .map(event -> {
                    String imageUrl = eventImageRepository.findByEvent(event)
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

                    return SearchEventDto.builder()
                            .eventId(event.getId())
                            .name(event.getName())
                            .address(event.getAddress())
                            .duration(event.getDuration())
                            .url(imageUrl)
                            .build();
                })
                .toList();

        return SearchEventsDto.builder()
                .pageInfo(PageInfo.convert(events, page))
                .searchEventDto(searchEventsDto)
                .build();
    }


    /* ================================================================= */
    //                          관리자 api                                 //
    /* ================================================================= */

    /**
     * 관리자가 보는 판매자들의 판매 요청 목록
     */
    public AdminApplyEventDto showAppliesOfSeller(Integer page, Integer size){
        Page<EventRequest> appliesOfSeller = eventRequestRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));

        return AdminApplyEventDto.builder()
                .pageInfo(PageInfo.convert(appliesOfSeller, page))
                .eventRequestsDto(getEventRequestsDto(appliesOfSeller))
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
     * 관리자의 판매자 요청 검색
     */
    public AdminApplyEventDto searchEventRequest(String name, String state, Integer page, Integer size){
        EEventRequestStatus eEventRequestStatus = EEventRequestStatus.toEnum(state);
        Page<EventRequest> eventRequestsDto;

        // 전체 상태 없이 전체 조회인 경우
        if (eEventRequestStatus == null){
            if (!state.equals(Constants.ALL))
                throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
            eventRequestsDto = eventRequestRepository.searchAllByName(
                    name,
                    PageRequest.of(page, size, Sort.by("createdAt").descending())
            );
        } else {
            eventRequestsDto = eventRequestRepository.searchAllByNameAndState(
                    name,
                    eEventRequestStatus,
                    PageRequest.of(page, size, Sort.by("createdAt").descending())
            );
        }

        return AdminApplyEventDto.builder()
                .pageInfo(PageInfo.convert(eventRequestsDto, page))
                .eventRequestsDto(getEventRequestsDto(eventRequestsDto))
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
            event.changeApproved(Boolean.TRUE);
        }

        EEventRequestStatus status = eventRequest.updateStatus(eEventRequestStatus);

        return ChangedEventRequestStatusDto.builder()
                .id(eventRequest.getId())
                .status(status.getStatus())
                .build();
    }

    /* ================================================================= */
    //                          판매자 api                                 //
    /* ================================================================= */
    /**
     * 판매 이력 조회
     */
    public MyEventHistoriesDto showMyHistories(Long userId, Integer page, Integer size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Page<EventRepository.EventHistory> myApprovedHistories = eventRepository.findAllMyApprovedHistories(
                user,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        List<EventHistoryDto> eventHistoriesDto = extracted(myApprovedHistories);

        return MyEventHistoriesDto.builder()
                .pageInfo(PageInfo.convert(myApprovedHistories, page))
                .eventHistoriesDto(eventHistoriesDto)
                .build();
    }
    /**
     * 판매자 판매 행사 검색
     */
    public MyEventHistoriesDto searchMyEvents(Long userId, String name, Integer page, Integer size){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Page<EventRepository.EventHistory> myEvents = eventRepository.searchAllByUserAndName(
                user,
                name,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        List<EventHistoryDto> eventHistoriesDto = extracted(myEvents);

        return MyEventHistoriesDto.builder()
                .pageInfo(PageInfo.convert(myEvents, page))
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
     * 페이지네이션 결과 dto로 변환하는 함수
     * @param mySearchResult
     * @return
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


    /**
     * 이벤트 요청 삭제 (이벤트도 삭제: cascade)
     * @param userId
     * @param eventRequestId
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
}
