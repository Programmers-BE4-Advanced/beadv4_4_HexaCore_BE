package com.back.chat.adapter.out;

import com.back.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    boolean existsByRoomIdAndUserIdAndContent(
            Long roomId,
            Long userId,
            String content
    );

    List<ChatMessage> findByRoomIdOrderByIdDesc(
            Long roomId,
            Pageable pageable
    );

    List<ChatMessage> findByRoomIdAndIdLessThanOrderByIdDesc(
            Long roomId,
            Long cursorMessageId,
            Pageable pageable
    );
}
