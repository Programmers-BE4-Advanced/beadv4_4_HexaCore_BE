package com.back.notification.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record NotificationCreatedEvent(
        List<String> notificationIds
) {
}
