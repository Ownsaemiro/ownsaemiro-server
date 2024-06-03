package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.EventHistoryDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventRequestStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventStatus;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ERole;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long> {

    /* ================================================================= */
    //                         사용자 api 관련 쿼리                          //
    /* ================================================================= */
    Optional<Event> findByIdAndIsApproved(Long id, Boolean isApproved);
    Page<Event> findAllByStatus(EEventStatus status, Pageable pageable);

    @Query("select e from Event e where e.category = :category and e.status = :status")
    Page<Event> findAllByStatusAndCategory(EEventStatus status, ECategory category, Pageable pageable);

    @Query("select e from Event e where e.name like %:name% and e.isApproved = true")
    Page<Event> searchAllByName(String name, Pageable pageable);


    /* ================================================================= */
    //                         판매자 api 관련 쿼리                          //
    /* ================================================================= */

    // 판매자 행사 이력 조회
    @Query("select e as event, er.createdAt as createdAt from Event e join EventRequest er on e.id = er.event.id where e.user = :user and e.isApproved = true")
    Page<EventHistory> findAllMyApprovedHistories(User user, Pageable pageable);

    // 판매자 행사 이력 검색
    @Query("select e as event, er.createdAt as createdAt from Event e join EventRequest er on e.id = er.event.id where e.user = :user and e.name like %:name% and e.isApproved = true")
    Page<EventHistory> searchAllByUserAndName(User user, String name, Pageable pageable);

    interface EventHistory {
        Event getEvent();
        LocalDate getCreatedAt();
        static EventRepository.EventHistory invoke(Event event, LocalDate createdAt){
            return new EventHistory() {

                @Override
                public Event getEvent() {
                    return event;
                }

                @Override
                public LocalDate getCreatedAt() {
                    return createdAt;
                }
            };
        }
    }

}
