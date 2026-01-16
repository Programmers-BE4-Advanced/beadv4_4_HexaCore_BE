package com.back.chat.dto.request;

public record ChatMessageSendRequestDto (
        Long roomId,
        String content
){
}
