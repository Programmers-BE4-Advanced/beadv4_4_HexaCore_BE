package com.back.chat.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomEnterRequestDto {

    private Long brandId;
    private Long userId;
}
