package com.back.notification.dto;

import lombok.Builder;

@Builder
public record PushDispatchMessage (
        String title,
        String body,
        String fcmToken,
        String deepLink
){
}
