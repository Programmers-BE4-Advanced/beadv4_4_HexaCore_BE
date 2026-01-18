package com.back.chat.adapter.in;

import com.back.chat.adapter.out.RedisChatMessagePublisher;
import com.back.chat.event.ChatMessageSavedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessagePublishListener {

    private final RedisChatMessagePublisher redisChatMessagePublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommit(ChatMessageSavedEvent event) {
        try {
            redisChatMessagePublisher.publish(event.roomId(), event.payload());
        } catch (Exception e) {
            log.error("Redis publish failed after commit. roomId={}, messageId={}",
                    event.roomId(),
                    event.payload().messageId(),
                    e
            );
        }
    }
}
