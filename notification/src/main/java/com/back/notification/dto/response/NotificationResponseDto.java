package com.back.notification.dto.response;

import com.back.notification.domain.enums.Type;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponseDto (
        String id,
        String title,
        String body,
        String deepLink,
        Type type,
        boolean isRead,
        LocalDateTime createdAt
) {
}
