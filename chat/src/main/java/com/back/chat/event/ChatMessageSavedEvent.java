package com.back.chat.event;


import com.back.chat.dto.response.ChatMessageSendResponseDto;

public record ChatMessageSavedEvent(
        Long roomId,
        ChatMessageSendResponseDto payload
) {}
