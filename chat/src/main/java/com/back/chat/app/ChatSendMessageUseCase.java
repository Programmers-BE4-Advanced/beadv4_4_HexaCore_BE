package com.back.chat.app;

import com.back.chat.domain.ChatMessage;
import com.back.chat.domain.ChatRoom;
import com.back.chat.dto.request.ChatMessageSendRequestDto;
import com.back.chat.dto.response.ChatMessageSendResponseDto;
import com.back.common.code.FailureCode;
import com.back.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatSendMessageUseCase {

    private final ChatSupport chatSupport;

    // Redis pub/sub 발행.
    // private final ChatMessagePublisher chatMessagePublisher;

    @Transactional
    public void sendMessage(ChatMessageSendRequestDto requestDto, Long userId) {

        ChatRoom chatRoom = chatSupport.findRoomById(requestDto.roomId())
                .orElseThrow(() -> new BadRequestException(FailureCode.CHAT_ROOM_NOT_FOUND));

        // (User 모듈 ApiClient 필요)
        // 사용자 메시지 전송 제한 여부 검증

        ChatMessage savedMessage = chatSupport.saveMessage(
                ChatMessage.create(chatRoom,userId,requestDto.content())
        );

        ChatMessageSendResponseDto payload = new ChatMessageSendResponseDto(
                savedMessage.getId(),
                chatRoom.getId(),
                userId,
                savedMessage.getContent(),
                savedMessage.isBlinded(),
                savedMessage.getCreatedAt()
        );

        // 모든 인스턴스에 메시지 전파
        // chatMessagePublisher.publish(room.getId(), payload);
    }

}
