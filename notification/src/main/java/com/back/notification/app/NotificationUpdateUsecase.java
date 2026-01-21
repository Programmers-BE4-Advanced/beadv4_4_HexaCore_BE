package com.back.notification.app;

import com.back.notification.adapter.out.NotificationRepository;
import com.back.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationUpdateUsecase {
    private final NotificationRepository notificationRepository;
    public void markUserNotificationAsRead(Notification notification) {
        notificationRepository.markAsRead(notification.getId());
    }
}
