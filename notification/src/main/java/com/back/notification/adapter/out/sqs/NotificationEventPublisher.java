package com.back.notification.adapter.out.sqs;

import com.back.notification.app.NotificationSupport;
import com.back.notification.app.NotificationUserSupport;
import com.back.notification.domain.Notification;
import com.back.notification.dto.NotificationCreatedEvent;
import com.back.notification.dto.PushDispatchMessage;
import com.back.notification.mapper.NotificationMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {
    private final SqsTemplate sqsTemplate;

    private final NotificationSupport notificationSupport;
    private final NotificationMapper mapper;
    private final NotificationUserSupport notificationUserSupport;

    @Value("${spring.cloud.aws.sqs.notification-queue}")
    private String queue;

    public void send(NotificationCreatedEvent event) {

        for (String notificationId : event.notificationIds()) {
            try {
                Notification notification =
                        notificationSupport.findById(notificationId);

                String fcmToken =
                        notificationUserSupport.findFcmToken(notification.getUserId());

                if (fcmToken == null) {
                    log.info("FCM 토큰 없음 - userId={}", notification.getUserId());
                    continue;
                }

                PushDispatchMessage payload =
                        mapper.toPushDispatchMessage(notification, fcmToken);

                sqsTemplate.send(to -> to
                        .queue(queue)
                        .payload(payload)
                );


            } catch (Exception e) {
                log.error(
                        "알림 전송 실패 - notificationId={}, error={}",
                        notificationId,
                        e.getMessage(),
                        e
                );
            }
        }
    }
}
