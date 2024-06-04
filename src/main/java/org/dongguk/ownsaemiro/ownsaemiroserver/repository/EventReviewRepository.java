package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.EventReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventReviewRepository extends JpaRepository<EventReview, Long> {
    List<EventReview> findTop3ByEvent(Event event);
}
