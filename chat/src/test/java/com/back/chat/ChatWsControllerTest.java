package com.back.chat;

import com.back.chat.adapter.in.ChatWsController;
import com.back.chat.app.ChatFacade;
import com.back.chat.dto.request.ChatMessageSendRequestDto;
import com.back.security.principal.AuthPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class ChatWsControllerTest {

    @Test
    void sendMessage_shouldCallFacade_withUserIdFromSession() {
        ChatFacade chatFacade = mock(ChatFacade.class);
        ChatWsController controller = new ChatWsController(chatFacade);

        ChatMessageSendRequestDto dto = new ChatMessageSendRequestDto(1L, "hello"); // record면 이렇게
        // class DTO면 new로 맞게 생성/세팅

        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("AUTH_PRINCIPAL", new AuthPrincipal(10L, "USER"));
        accessor.setSessionAttributes(attrs);

        controller.sendMessage(dto, accessor);

        verify(chatFacade, times(1)).sendMessage(dto, 10L);
    }
}
