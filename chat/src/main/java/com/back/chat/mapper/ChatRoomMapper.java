package com.back.chat.mapper;

import com.back.chat.domain.ChatRoom;
import com.back.chat.dto.response.ChatRoomEnterResponseDto;

public class ChatRoomMapper {
    private static final String CHAT_ROOM_TOPIC_PREFIX = "/topic/chat/room/";

    public static ChatRoomEnterResponseDto toEnterResponseDto(ChatRoom chatRoom) {
        return new ChatRoomEnterResponseDto(chatRoom.getId(), CHAT_ROOM_TOPIC_PREFIX + chatRoom.getId());
    }
}
