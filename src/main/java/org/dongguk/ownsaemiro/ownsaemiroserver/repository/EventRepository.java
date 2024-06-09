package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ECategory;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long> {
    /* ================================================================= */
    //                         행사 관련 scheduler                         //
    /* ================================================================= */

    /**
     *  행사 상태 변경
     */
    @Query("select e from Event e where e.isApproved = true and e.status = :status")
    List<Event> findAllByStatus(EEventStatus status);

    /* ================================================================= */
    //                          홈 api 관련 쿼리                            //
    /* ================================================================= */
    /**
     * 인기 있는 행사 조회
     */
    @Query("select e from Event e " +
            "join UserLikedEvent ul " +
            "on e.id = ul.event.id " +
            "where e.isApproved = true " +
            "group by e.id " +
            "order by count(ul.id) desc limit 3")
    List<Event> findPopularEvents();

    @Query("select e from Event e where e.isApproved = true order by rand() limit 3")
    List<Event> findTop3ByRandomOrder();

    @Query("select e from Event e where e.isApproved = true order by rand() limit 5")
    List<Event> findTop5ByRandomOrder();

    /* ================================================================= */
    //                         사용자 api 관련 쿼리                          //
    /* ================================================================= */
    Optional<Event> findByIdAndIsApproved(Long id, Boolean isApproved);
    @Query("select e from Event e where e.isApproved = true")
    Page<Event> findAllWithPaging(Pageable pageable);

    @Query("select e from Event e where e.category = :category and  e.isApproved = true")
    Page<Event> findAllByCategory(ECategory category, Pageable pageable);

    @Query(value = "select * from events e where e.is_approved = true and match(e.name, e.description) against(:name in boolean mode)", nativeQuery = true)
    Page<Event> searchAllByName(@Param("name") String name, Pageable pageable);


    /* ================================================================= */
    //                         판매자 api 관련 쿼리                          //
    /* ================================================================= */

    // 판매자 행사 이력 조회
    @Query("select e as event, er.createdAt as createdAt from Event e join EventRequest er on e.id = er.event.id where e.user = :user and e.isApproved = true")
    Page<EventHistory> findAllMyApprovedHistories(User user, Pageable pageable);

    // 판매자 행사 이력 이름으로 검색
    @Query("select e as event, er.createdAt as createdAt from Event e join EventRequest er on e.id = er.event.id where e.user = :user and e.name like %:name% and e.isApproved = true")
    Page<EventHistory> findAllMyApprovedHistoriesByName(User user, String name, Pageable pageable);

    // 판매자 행사 이력 상태로 검색
    @Query("select e as event, er.createdAt as createdAt from Event e join EventRequest er on e.id = er.event.id where e.user = :user and e.status =:status and e.isApproved = true")
    Page<EventHistory> findAllMyApprovedHistoriesByStatus(User user, EEventStatus status, Pageable pageable);

    // 판매자 행사 이력 이름과 상태로 검색
    @Query("select e as event, er.createdAt as createdAt from Event e join EventRequest er on e.id = er.event.id where e.user = :user and e.name like %:name% and e.status =:status and e.isApproved = true")
    Page<EventHistory> findAllMyApprovedHistoriesByNameAndStatus(User user, String name, EEventStatus status, Pageable pageable);


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
