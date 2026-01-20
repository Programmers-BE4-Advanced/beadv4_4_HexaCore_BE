package com.back.chat.adapter.out;

import com.back.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    boolean existsByRoomIdAndUserIdAndContent(
            Long roomId,
            Long userId,
            String content
    );
}
