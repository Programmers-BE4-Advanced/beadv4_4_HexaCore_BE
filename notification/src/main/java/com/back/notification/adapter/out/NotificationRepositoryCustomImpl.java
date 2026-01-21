package com.back.notification.adapter.out;

import com.back.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom{
    private final MongoTemplate mongoTemplate;

    @Override
    public void markAsRead(String notificationId) {
        Query query = Query.query(Criteria.where("_id").is(notificationId));
        Update update = new Update()
                .set("isRead", true)
                .set("updatedAt", LocalDateTime.now());

        mongoTemplate.updateFirst(query, update, Notification.class);
    }
}
