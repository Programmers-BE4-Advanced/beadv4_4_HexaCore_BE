package com.back.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ChatMessageSendRequestDto (
        @NotNull
        @Positive
        Long roomId,

        @NotBlank
        @Size(max = 1000)
        String content
){
}
