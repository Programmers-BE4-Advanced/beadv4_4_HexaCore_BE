package com.back.chat.event.payload;

import java.time.LocalDateTime;

public record ChatMessagePayload(
        Long messageId,
        Long userId,
        Long roomId,
        String content,
        boolean isBlinded,
        LocalDateTime createdAt
){
}
