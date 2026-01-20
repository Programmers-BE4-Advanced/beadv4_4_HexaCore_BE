package com.back.chat.app;

import com.back.chat.adapter.out.ChatMessageRepository;
import com.back.chat.adapter.out.ChatReportRepository;
import com.back.chat.adapter.out.ChatRoomRepository;
import com.back.chat.domain.ChatMessage;
import com.back.chat.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.data.domain.Pageable;
import java.util.List;
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

    public boolean existsRoomById(Long roomId) { return chatRoomRepository.existsById(roomId); }

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findByRoomIdOrderByIdDesc(Long roomId, Pageable pageable){
        return chatMessageRepository.findByRoomIdOrderByIdDesc(roomId, pageable);
    }

    public List<ChatMessage> findByRoomIdAndIdLessThanOrderByIdDesc(Long roomId, Long cursorMessageId, Pageable pageable){
        return chatMessageRepository.findByRoomIdAndIdLessThanOrderByIdDesc(roomId, cursorMessageId, pageable);
    }


}
