package com.back.chat.adapter.in;

import com.back.chat.app.ChatFacade;
import com.back.chat.dto.request.ChatMessageSendRequestDto;
import com.back.chat.dto.response.ChatMessageSendResponseDto;
import com.back.common.response.CommonResponse;
import com.back.security.principal.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat-ws")
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatFacade chatFacade;

    private static final String ATTR_PRINCIPAL = "AUTH_PRINCIPAL";

    @MessageMapping("/message")
    public void sendMessage(
            @Payload ChatMessageSendRequestDto requestDto,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Object principalObj = headerAccessor.getSessionAttributes().get(ATTR_PRINCIPAL);

        if (!(principalObj instanceof AuthPrincipal)) {
            throw new IllegalStateException("WebSocket 인증 정보(AuthPrincipal)가 없습니다.");
        }

        AuthPrincipal authPrincipal = (AuthPrincipal) principalObj;
        Long userId = authPrincipal.getUserId();

        chatFacade.sendMessage(requestDto, userId);
    }
}
