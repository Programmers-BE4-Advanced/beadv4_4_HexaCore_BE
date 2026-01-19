package com.back.chat;

import com.back.chat.adapter.out.ChatMessageRepository;
import com.back.chat.adapter.out.ChatRoomRepository;
import com.back.chat.app.ChatSendMessageUseCase;
import com.back.chat.domain.ChatRoom;
import com.back.chat.dto.request.ChatMessageSendRequestDto;
import com.back.chat.event.payload.ChatMessagePayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatE2EIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    ChatSendMessageUseCase chatSendMessageUseCase;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    ChatMessageRepository chatMessageRepository;

    private WebSocketStompClient stompClient;

    @BeforeEach
    void setUp() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
    }

    @Test
    void sendMessage_persistsToDb_and_broadcastsViaWebSocket() throws Exception {
        // given: 채팅방 생성
        ChatRoom room = chatRoomRepository.save(new ChatRoom());
        Long roomId = room.getId();
        Long userId = 1L;

        long beforeCount = chatMessageRepository.count();

        // WebSocket 구독 준비
        CompletableFuture<ChatMessagePayload> receivedFuture = new CompletableFuture<>();
        StompSession session = connect();

        String topic = "/topic/chat/room/" + roomId;
        session.subscribe(topic, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessagePayload.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                receivedFuture.complete((ChatMessagePayload) payload);
            }
        });

        // when: 메시지 전송 (DB 저장 + Redis 발행 트리거)
        String content = "hello";
        chatSendMessageUseCase.sendMessage(
                new ChatMessageSendRequestDto(roomId, content),
                userId
        );

        // then 1) DB 저장 검증 (커밋 타이밍 감안해 폴링)
        boolean saved = waitUntilTrue(3, TimeUnit.SECONDS,
                () -> chatMessageRepository.count() == beforeCount + 1);
        assertThat(saved).isTrue();

        // then 2) WebSocket 브로드캐스트 수신 검증
        ChatMessagePayload received = receivedFuture.get(3, TimeUnit.SECONDS);

        assertThat(received.roomId()).isEqualTo(roomId);
        assertThat(received.userId()).isEqualTo(userId);

        // 블라인드 정책 검증(선택)
        if (received.isBlinded()) {
            assertThat(received.content()).isEqualTo("블라인드된 메시지입니다");
        } else {
            assertThat(received.content()).isEqualTo(content);
        }
    }

    private StompSession connect() throws Exception {
        String url = "ws://localhost:" + port + "/ws/chat";

        CompletableFuture<StompSession> future =
                stompClient.connectAsync(url, new StompSessionHandlerAdapter() {});
        return future.get(3, TimeUnit.SECONDS);
    }

    private boolean waitUntilTrue(long timeout, TimeUnit unit, CheckedSupplier<Boolean> condition) {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        while (System.nanoTime() < deadline) {
            try {
                if (Boolean.TRUE.equals(condition.get())) return true;
            } catch (Exception ignored) {}
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }
        return false;
    }

    @FunctionalInterface
    interface CheckedSupplier<T> {
        T get() throws Exception;
    }
}
