package com.back.notification.app;

import com.back.notification.app.strategy.NotificationStrategyRegistry;
import com.back.notification.domain.Notification;
import com.back.notification.app.strategy.NotificationStrategy;
import com.back.notification.dto.NotificationMessage;
import com.back.notification.domain.enums.Type;
import com.back.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class NotificationFacade {

    private final NotificationSaveUsecase notificationSaveUsecase;
    private final NotificationStrategyRegistry strategyRegistry;
    private final NotificationMessageFactory notificationMessageFactory;
    private final NotificationMapper mapper;

    private final ApplicationEventPublisher eventPublisher;

    public <T> void notify(Type type, T event) {
        NotificationStrategy<T> strategy = strategyRegistry.get(type);

        List<Notification> notifications = strategy.create(event);

        Locale locale = Locale.KOREA;
        notifications.forEach(notification -> {
            NotificationMessage message = notificationMessageFactory.create(notification, locale);
            notification.applyMessage(message, locale);
        });

        notificationSaveUsecase.saveAll(notifications);

        eventPublisher.publishEvent(mapper.toNotificationCreatedEvent(notifications));
    }

}
