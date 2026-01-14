package com.back.chat.mapper;

import com.back.chat.domain.ChatRoom;
import com.back.chat.dto.request.ChatRoomEnterRequestDto;
import com.back.chat.dto.response.ChatRoomEnterResponseDto;

public class ChatRoomMapper {
    public static ChatRoomEnterRequestDto toEnterRequestDto(Long brandId, Long userId){
        return ChatRoomEnterRequestDto.builder()
                .brandId(brandId)
                .userId(userId)
                .build();
    }

    public static ChatRoomEnterResponseDto toEnterResponseDto(ChatRoom chatRoom){
        return ChatRoomEnterResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .subscribeTopic("/topic/chat/room/" + chatRoom.getId())
                .build();
    }
}
