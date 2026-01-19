package com.back.chat.adapter.out;

import com.back.chat.event.payload.ChatMessagePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisChatMessagePublisher {

    private static final String CHANNEL_PREFIX = "chatroom:";

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public void publish(Long roomId, ChatMessagePayload payload) {
        String channel = CHANNEL_PREFIX + roomId;

        try {
            String message = objectMapper.writeValueAsString(payload);
            stringRedisTemplate.convertAndSend(channel, message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Redis 채팅 메시지 발행 실패", e);
        }
    }
}
