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
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatE2EIntegrationTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @LocalServerPort
    int port;

    @Autowired
    ChatSendMessageUseCase chatSendMessageUseCase; // TODO: 너 클래스명 그대로면 OK
    @Autowired
    ChatRoomRepository chatRoomRepository;         // TODO: repo 이름 맞추기
    @Autowired
    ChatMessageRepository chatMessageRepository;   // TODO: repo 이름 맞추기

    private WebSocketStompClient stompClient;

    @BeforeEach
    void setUp() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        // Spring 7/Boot 4: MappingJackson2MessageConverter deprecated 피하려고 설정 안 함 (기본으로 충분)
    }

    @Test
    void sendMessage_persistsToDb_and_broadcastsViaWebSocket() throws Exception {
        // given: 채팅방 생성
        ChatRoom room = chatRoomRepository.save(new ChatRoom(/* TODO: 생성자/필드에 맞게 */));
        Long roomId = room.getId();
        Long userId = 1L;

        // WebSocket subscribe 준비
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

        // when: 메시지 전송(= DB 저장 + Redis 발행 트리거)
        String content = "hello";
        chatSendMessageUseCase.sendMessage(
                new ChatMessageSendRequestDto(roomId, content), // TODO: DTO 생성자 맞추기
                userId
        );

        // then 1) DB 저장 검증 (커밋/비동기 감안해서 폴링)
        // ✅ 가장 안전한 검증: roomId + userId + content(또는 최신 1건) 조회
        boolean dbSaved = waitUntilTrue(3, TimeUnit.SECONDS, () ->
                        chatMessageRepository.existsByRoomIdAndUserIdAndContent(roomId, userId, content)
                // TODO: 네 repository 메서드에 맞게 바꾸기
        );
        assertThat(dbSaved).isTrue();

        // then 2) WebSocket 브로드캐스트 수신 검증
        ChatMessagePayload received = receivedFuture.get(3, TimeUnit.SECONDS);

        assertThat(received.roomId()).isEqualTo(roomId);
        assertThat(received.userId()).isEqualTo(userId);

        // 블라인드 정책 검증까지 같이 가능
        if (received.isBlinded()) {
            assertThat(received.content()).isEqualTo("블라인드된 메시지입니다");
        } else {
            assertThat(received.content()).isEqualTo(content);
        }
    }

    private StompSession connect() throws Exception {
        // TODO: 너 WebSocket endpoint에 맞게 수정 (예: /ws/chat)
        String url = "ws://localhost:" + port + "/ws/chat";

        CompletableFuture<StompSession> future =
                stompClient.connectAsync(url, new StompSessionHandlerAdapter() {});
        return future.get(3, TimeUnit.SECONDS);
    }

    private boolean waitUntilTrue(long timeout, TimeUnit unit, CheckedSupplier<Boolean> condition) {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        while (System.nanoTime() < deadline) {
            try {
                if (Boolean.TRUE.equals(condition.get())) {
                    return true;
                }
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
