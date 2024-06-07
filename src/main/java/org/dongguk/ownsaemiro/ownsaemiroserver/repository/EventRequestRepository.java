package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.EventRequest;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {

    /* ================================================================= */
    //                         관리자 api 관련 쿼리                          //
    /* ================================================================= */

    // 행사명 검색
    @Query("select er from EventRequest er join er.event e where e.name like %:name% order by er.createdAt desc")
    Page<EventRequest> searchAllByName(String name, Pageable pageable);

    // 행사명 & 상태 검색
    @Query("select er from EventRequest er join er.event e where e.name like %:name% and er.state = :status order by er.createdAt desc")
    Page<EventRequest> searchAllByNameAndState(String name, EEventRequestStatus status, Pageable pageable);

    /* ================================================================= */
    //                         판매자 api 관련 쿼리                          //
    /* ================================================================= */

    Optional<EventRequest> findById(Long id);

    Page<EventRequest> findAll(Pageable pageable);

    Page<EventRequest> findAllByUser(User user, Pageable pageable);

    @Query("SELECT er FROM EventRequest er JOIN er.event e WHERE e.name LIKE %:searchString% AND er.user = :user")
    Page<EventRequest> findAllMyEventRequestsByName(User user, String searchString, Pageable pageable);

    @Query("SELECT er FROM EventRequest er JOIN er.event e WHERE er.state = :status AND er.user = :user")
    Page<EventRequest> findAllMyEventRequestsByStatus(User user, EEventRequestStatus status, Pageable pageable);

    @Query("SELECT er FROM EventRequest er JOIN er.event e WHERE e.name LIKE %:searchString% AND er.user = :user AND er.state = :status")
    Page<EventRequest> findAllMyEventRequestsByNameAndStatus(User user, EEventRequestStatus status, String searchString, Pageable pageable);
}
