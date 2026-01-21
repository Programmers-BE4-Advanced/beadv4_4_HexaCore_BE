package com.back.notification.adapter.in.sqs;

import com.back.notification.adapter.out.sqs.NotificationEventPublisher;
import com.back.notification.dto.NotificationCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationDispatchEventListener {
    private final NotificationEventPublisher publisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(NotificationCreatedEvent event) {
        publisher.send(event);
    }
}
