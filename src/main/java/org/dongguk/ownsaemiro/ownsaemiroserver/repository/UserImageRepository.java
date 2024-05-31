package org.dongguk.ownsaemiro.ownsaemiroserver.repository;

import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Image;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<Image> findByUser(User user);
}
