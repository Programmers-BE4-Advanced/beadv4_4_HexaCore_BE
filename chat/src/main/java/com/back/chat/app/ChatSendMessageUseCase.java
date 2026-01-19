package com.back.chat.app;

import com.back.chat.domain.ChatMessage;
import com.back.chat.dto.request.ChatMessageSendRequestDto;
import com.back.chat.event.payload.ChatMessagePayload;
import com.back.chat.event.ChatMessageSavedEvent;
import com.back.common.code.FailureCode;
import com.back.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatSendMessageUseCase {

    private final ChatSupport chatSupport;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void sendMessage(ChatMessageSendRequestDto requestDto, Long userId) {

        Long roomId = requestDto.roomId();

        if(!chatSupport.existsRoomById(roomId)){
            throw new BadRequestException(FailureCode.CHAT_ROOM_NOT_FOUND);
        }

        // TODO 사용자 메시지 전송 제한 여부 검증 (외부 모듈/ApiClient)

        ChatMessage savedMessage = chatSupport.saveMessage(
                ChatMessage.create(roomId,userId,requestDto.content())
        );

        ChatMessagePayload payload = new ChatMessagePayload(
                savedMessage.getId(),
                roomId,
                userId,
                savedMessage.getContent(),
                savedMessage.isBlinded(),
                savedMessage.getCreatedAt()
        );

        eventPublisher.publishEvent(new ChatMessageSavedEvent(roomId, payload));
    }

}
