package com.back.notification.adapter.out;

import com.back.notification.domain.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String>,
        NotificationRepositoryCustom {
    Slice<Notification> findPageByUserId(Pageable pageable, Long userId);
}
