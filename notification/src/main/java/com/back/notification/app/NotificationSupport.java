package com.back.notification.app;

import com.back.notification.adapter.out.NotificationRepository;
import com.back.notification.domain.Notification;
import com.back.notification.domain.NotificationUser;
import com.back.notification.exception.NotificationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@Configuration
@RequiredArgsConstructor
public class NotificationSupport {
    private final NotificationRepository notificationRepository;

   public Notification findById(String id) {
       return notificationRepository.findById(id)
               .orElseThrow(NotificationNotFoundException::new);
   }

    public Slice<Notification> findRecentNotifications(NotificationUser user, Pageable pageable) {
       return notificationRepository
               .findPageByUserId(pageable, user.getId());
    }
}
