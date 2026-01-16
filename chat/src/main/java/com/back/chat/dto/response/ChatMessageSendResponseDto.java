package com.back.chat.dto.response;

import java.time.LocalDateTime;

public record ChatMessageSendResponseDto (
        Long messageId,
        Long userId,
        Long roomId,
        String content,
        boolean isBlinded,
        LocalDateTime createdAt
){
}
