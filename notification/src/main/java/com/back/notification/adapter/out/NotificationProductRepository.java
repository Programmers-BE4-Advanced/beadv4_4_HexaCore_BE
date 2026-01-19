package com.back.notification.adapter.out;

import com.back.notification.domain.NotificationProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationProductRepository extends JpaRepository<NotificationProduct, Long> {
}
