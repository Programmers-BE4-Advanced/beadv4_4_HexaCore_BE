package com.back.notification.app;

import com.back.notification.adapter.out.NotificationUserRepository;
import com.back.notification.domain.NotificationUser;
import com.back.notification.exception.NotificationUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationUserSupport {
    private final NotificationUserRepository notificationUserRepository;

    public String findFcmToken(Long userId) {
        return notificationUserRepository.findFcmTokenByUserId(userId)
                .orElse(null);
    }

    public NotificationUser findById(Long userId) {
        return notificationUserRepository.findById(userId)
                .orElseThrow(NotificationUserNotFoundException::new);
    }
}
