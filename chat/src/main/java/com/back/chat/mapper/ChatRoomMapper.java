package com.back.chat.mapper;

import com.back.chat.domain.ChatRoom;
import com.back.chat.dto.request.ChatRoomEnterRequestDto;
import com.back.chat.dto.response.ChatRoomEnterResponseDto;

public class ChatRoomMapper {
    public static ChatRoomEnterRequestDto toEnterRequestDto(Long brandId, Long userId) {
        return new ChatRoomEnterRequestDto(brandId, userId);
    }

    public static ChatRoomEnterResponseDto toEnterResponseDto(ChatRoom chatRoom) {
        return new ChatRoomEnterResponseDto(chatRoom.getId(), "/topic/chat/room/" + chatRoom.getId());
    }
}
