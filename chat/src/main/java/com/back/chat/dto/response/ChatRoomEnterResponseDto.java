package com.back.chat.dto.response;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class ChatRoomEnterResponseDto {
    private Long chatRoomId;

    private String subscribeTopic;
}
