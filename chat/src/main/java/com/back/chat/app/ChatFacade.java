package com.back.chat.app;


import com.back.chat.dto.request.ChatRoomEnterRequestDto;
import com.back.chat.dto.response.ChatRoomEnterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatFacade {

    private final ChatEnterChatRoomUseCase chatEnterChatRoomUseCase;

    @Transactional
    public ChatRoomEnterResponseDto enterChatRoom(ChatRoomEnterRequestDto dto){
        return chatEnterChatRoomUseCase.enterChatRoom(dto);
    }



}
