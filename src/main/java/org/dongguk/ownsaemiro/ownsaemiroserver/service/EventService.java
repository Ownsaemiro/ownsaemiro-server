package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventRequestStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ETicketStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.*;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.AuthUtil;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserImageRepository userImageRepository;
    private final EventImageRepository eventImageRepository;
    private final EventReviewRepository eventReviewRepository;
    private final UserLikedEventRepository userLikedEventRepository;

    /* ================================================================= */
    //                         행사 관련 scheduler                         //
    /* ================================================================= */
    @Transactional
    public void updateEventState(){
        // 진행 -> 종료
        LocalDate now = LocalDate.now();
        eventRepository.findAllByStatus(EEventStatus.SELLING)
                .forEach(event -> {
                    LocalDate endDate = LocalDate.parse(event.getDuration().split(Constants.STR_CONNECTOR)[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (now.isAfter(endDate)){
                        event.changeStatus(EEventStatus.COMPLETE);
                    }
                });
    }

    /* ================================================================= */
    //                            홈 화면 api                              //
    /* ================================================================= */
    public PopularEventsDto showPopularEvents(){

        List<Event> events = eventRepository.findPopularEvents();

        List<PopularEventDto> popularEventsDto = events.stream()
                .map(event -> {
                    String image = eventImageRepository.findByEvent(event)
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

                    return PopularEventDto.builder()
                            .id(event.getId())
                            .image(image)
                            .name(event.getName())
                            .duration(event.getDuration())
                            .build();
                }).toList();

        return PopularEventsDto.builder()
                .eventsDto(popularEventsDto)
                .build();
    }

    public RecommendEventDtos showRecommendEvents() {

        // 랜덤으로 5개의 이벤트를 추천
        List<Event> events = eventRepository.findTop5ByRandomOrder();

        List<RecommendEventDtos.RecommendEventDto> recommendEventDtos = events.stream()
                .map(event -> {
                    String image = eventImageRepository.findByEvent(event)
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

                    return RecommendEventDtos.RecommendEventDto.builder()
                            .id(event.getId())
                            .title(event.getName())
                            .image(image)
                            .duration(event.getDuration())
                            .address(event.getAddress())
                            .build();
                }).toList();

        return RecommendEventDtos.builder()
                .events(recommendEventDtos)
                .build();
    }


    /* ================================================================= */
    //                           사용자 행사 api                            //
    /* ================================================================= */


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
        } else if (category != null) {
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
     * 행사 상세 정보 보기 - 남은 좌석 개수
     */
    public RemainingSeatDto showRemainingSeat(Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        return RemainingSeatDto.builder()
                .remainingSeat(Math.toIntExact(event.getSeat() - ticketRepository.countByEvent(event) + ticketRepository.countAvailableTickets(event)))
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
     * 행사 상세 정보 보기 - review
     */
    public ReviewsOfEventDto showReviewsOfEvent(Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));


        List<ReviewOfEventDto> reviewOfEventsDto = eventReviewRepository.findTop3ByEvent(event).stream()

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
                .reviewsDto(reviewOfEventsDto)
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
     * 행사 리뷰 목록 불러오기
     */
    public AllReviewsOfEventDto showAllReviewsOfEvent(Long eventId, Integer page, Integer size){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EVENT));

        Page<EventReview> reviewsOfEvent = eventReviewRepository.findAllByEvent(
                event,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        List<ReviewOfEventDto> reviewOfEventsDto = reviewsOfEvent.getContent().stream()
                .map(eventReview -> {
                    User user = eventReview.getUser();

                    String image = userImageRepository.findByUser(user)
                            .map(Image::getUrl)
                            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_IMAGE));

                    return ReviewOfEventDto.builder()
                            .id(user.getId())
                            .nickname(user.getNickname())
                            .image(image)
                            .content(eventReview.getContent())
                            .build();
                }).toList();

        return AllReviewsOfEventDto.builder()
                .pageInfo(PageInfo.convert(reviewsOfEvent, page))
                .reviewsDto(reviewOfEventsDto)
                .build();
    }

    /**
     * 행사 검색
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

}
