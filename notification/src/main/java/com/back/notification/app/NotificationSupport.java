package com.back.notification.app;

import com.back.notification.adapter.out.NotificationRepository;
import com.back.notification.domain.Notification;
import com.back.notification.exception.NotificationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NotificationSupport {
    private final NotificationRepository notificationRepository;

   public Notification findById(String id) {
       return notificationRepository.findById(id)
               .orElseThrow(NotificationNotFoundException::new);
   }

}
