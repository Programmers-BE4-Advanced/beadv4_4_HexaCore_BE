package com.back.chat.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageHistoryResponseDto(
        Long roomId,
        List<ChatMessageItemDto> messages,
        Long nextCursorMessageId,
        boolean hasNext
) {
    public record ChatMessageItemDto(
            Long messageId,
            Long userId,
            String content,
            boolean isBlinded,
            LocalDateTime createdAt
    ) {}
}
