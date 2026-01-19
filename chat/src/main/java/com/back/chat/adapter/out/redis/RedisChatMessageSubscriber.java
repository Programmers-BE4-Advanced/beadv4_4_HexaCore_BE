package com.back.chat.adapter.out.redis;

import com.back.chat.domain.ChatMessageBlindPolicy;
import com.back.chat.event.payload.ChatMessagePayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChatMessageSubscriber implements MessageListener {

    private static final String ROOM_TOPIC_PREFIX = "/topic/chat/room/";

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageBlindPolicy chatMessageBlindPolicy;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        String rawBody = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            ChatMessagePayload payload =
                    objectMapper.readValue(rawBody, ChatMessagePayload.class);

            ChatMessagePayload applied = chatMessageBlindPolicy.apply(payload);

            String destination = roomTopic(applied.roomId());
            messagingTemplate.convertAndSend(destination, applied);

            log.info("[CHAT][REDIS-SUB] channel={}, roomId={}, messageId={}, blinded={}, destination={}",
                    channel, applied.roomId(), applied.messageId(), applied.isBlinded(), destination);

        } catch (Exception e) {
            log.error("[CHAT][REDIS-SUB][ERROR] channel={}, rawBody={}", channel, rawBody, e);
        }
    }

    private String roomTopic(Long roomId) {
        return ROOM_TOPIC_PREFIX + roomId;
    }
}
