package com.back.chat.event;


import com.back.chat.event.payload.ChatMessagePayload;

public record ChatMessageSavedEvent(
        Long roomId,
        ChatMessagePayload payload
) {}
