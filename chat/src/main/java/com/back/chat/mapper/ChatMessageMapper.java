package com.back.chat.mapper;

import com.back.chat.domain.ChatMessage;
import com.back.chat.dto.response.ChatMessageHistoryResponseDto;

import java.util.List;

public class ChatMessageMapper {

    public static ChatMessageHistoryResponseDto toHistoryResponseDto(Long roomId, List<ChatMessageHistoryResponseDto.ChatMessageItemDto> itemDtoList, Long nextCursorMessageId, boolean hasNext) {
        return new ChatMessageHistoryResponseDto(
                roomId,
                itemDtoList,
                nextCursorMessageId,
                hasNext
        );
    }

    public static ChatMessageHistoryResponseDto.ChatMessageItemDto toItemDto(ChatMessage m) {
        return new ChatMessageHistoryResponseDto.ChatMessageItemDto(
                m.getId(),
                m.getUserId(),
                m.isBlinded() ? "블라인드된 메시지입니다." : m.getContent(),
                m.isBlinded(),
                m.getCreatedAt()
        );
    }

}
