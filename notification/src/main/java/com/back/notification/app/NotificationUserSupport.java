package com.back.notification.app;

import com.back.notification.adapter.out.NotificationUserRepository;
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
}
