package com.back.notification.app;

import com.back.notification.adapter.out.NotificationProductRepository;
import com.back.notification.domain.NotificationProduct;
import com.back.notification.exception.NotificationProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProductUsecase {
    private final NotificationProductRepository notificationProductRepository;

    public NotificationProduct findNotificationProductById(Long productId) {
        return notificationProductRepository.findById(productId)
                .orElseThrow(NotificationProductNotFoundException::new);
    }
}
