package com.back.chat.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChatMessageHistoryRequestDto(
        @NotNull
        Long roomId,
        Long cursorMessageId,
        Integer size
) {
}
