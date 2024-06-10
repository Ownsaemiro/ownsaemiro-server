package org.dongguk.ownsaemiro.ownsaemiroserver.service;


import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.Notification;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.NotificationIdDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.NotificationDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.NotificationsDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.PageInfo;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.NotificationRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    /**
     * 사용자 알림 목록 조회
     */
    public NotificationsDto showNotificationsOfUser(Long userId, Integer page, Integer size){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Page<Notification> notifications = notificationRepository.findAllByUser(
                user,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        List<NotificationDto> notificationsDto = notifications.getContent().stream()
                .map(notification -> NotificationDto.builder()
                        .id(notification.getId())
                        .title(notification.getTitle())
                        .body(notification.getContent())
                        .build()
                ).toList();

        return NotificationsDto.builder()
                .pageInfo(PageInfo.convert(notifications, page))
                .notificationsDto(notificationsDto)
                .build();
    }

    /**
     * 사용자 알림 삭제 (사용자 알림 읽음처리)
     */
    @Transactional
    public void deleteNotification(NotificationIdDto notificationIdDto){
        Notification notification = notificationRepository.findById(notificationIdDto.notificationId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_NOTIFICATION));

        notificationRepository.delete(notification);
    }
}
