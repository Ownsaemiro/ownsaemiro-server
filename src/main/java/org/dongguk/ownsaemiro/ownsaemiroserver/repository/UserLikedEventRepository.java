package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserLikedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLikedEventRepository extends JpaRepository<UserLikedEvent, Long> {
    Boolean existsByUserAndEvent(User user, Event event);

    Optional<UserLikedEvent> findByUserAndEvent(User user, Event event);

    Page<UserLikedEvent> findAllByUser(User user, Pageable pageable);
}
