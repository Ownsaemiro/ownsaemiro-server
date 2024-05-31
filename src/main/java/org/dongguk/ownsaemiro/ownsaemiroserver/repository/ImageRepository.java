package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
