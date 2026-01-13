package com.back.chat.app;

import com.back.chat.domain.ChatRoom;
import com.back.chat.dto.request.ChatRoomEnterRequestDto;
import com.back.chat.dto.response.ChatRoomEnterResponseDto;
import com.back.common.code.FailureCode;
import com.back.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatEnterChatRoomUseCase {
    private final ChatSupport chatSupport;
    public static final String ROOM_TOPIC_PREFIX = "/topic/chat/room/";


    public ChatRoomEnterResponseDto enterChatRoom(ChatRoomEnterRequestDto dto) {
        ChatRoom chatRoom = chatSupport.findRoomByBrandId(dto.getBrandId()).orElseThrow(()-> new BadRequestException(FailureCode.ENTITY_NOT_FOUND));
        return ChatRoomEnterResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .subscribeTopic(ROOM_TOPIC_PREFIX + chatRoom.getId())
                .build();
    }
}
