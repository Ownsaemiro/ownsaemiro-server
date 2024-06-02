package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.EventRequest;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.EEventRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    Page<EventRequest> findAllByUser(User user, Pageable pageable);

    @Query("SELECT er FROM EventRequest er JOIN er.event e WHERE e.name LIKE %:searchString% AND er.user = :user")
    Page<EventRequest> searchAllByUser(User user, String searchString, Pageable pageable);

    @Query("SELECT er FROM EventRequest er JOIN er.event e WHERE e.name LIKE %:searchString% AND er.user = :user AND er.state = :status")
    Page<EventRequest> searchAllByUserAndState(User user, EEventRequestStatus status, String searchString, Pageable pageable);
}
