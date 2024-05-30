package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Event;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.EventImage;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    Optional<Image> findByEvent(Event event);
}
