package com.back.notification.app.strategy;

import com.back.notification.domain.Notification;
import com.back.notification.domain.enums.Type;

import java.util.List;

public interface NotificationStrategy<T> {
    Type type();
    List<Notification> create(T dto);
}
