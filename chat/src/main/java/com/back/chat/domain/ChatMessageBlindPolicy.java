package com.back.chat.domain;

import com.back.chat.event.payload.ChatMessagePayload;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageBlindPolicy {

    private static final String BLINDED_TEXT = "블라인드된 메시지입니다";

    public ChatMessagePayload apply(ChatMessagePayload payload) {
        if (!payload.isBlinded()) {
            return payload;
        }
        return new ChatMessagePayload(
                payload.messageId(),
                payload.userId(),
                payload.roomId(),
                BLINDED_TEXT,
                true,
                payload.createdAt()
        );
    }
}
