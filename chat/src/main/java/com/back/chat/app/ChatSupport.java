package com.back.chat.app;

import com.back.chat.adapter.out.ChatMessageRepository;
import com.back.chat.adapter.out.ChatReportRepository;
import com.back.chat.adapter.out.ChatRoomRepository;
import com.back.chat.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatSupport {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatReportRepository chatReportRepository;
    private final ChatMessageRepository chatMessageRepository;

    public Optional<ChatRoom> findRoomByBrandId(Long brandId){
        return chatRoomRepository.findByBrandId(brandId);
    }


}
