package com.back.notification.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record NotificationListResponseDto(
        List<NotificationResponseDto> notificationResponses,
        boolean hasNext
) {
}
