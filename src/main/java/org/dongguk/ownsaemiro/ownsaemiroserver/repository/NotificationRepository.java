package org.dongguk.ownsaemiro.ownsaemiroserver.repository;


import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Notification;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByUser(User user, Pageable pageable);
}
