package com.back.notification.app;

import com.back.notification.adapter.out.NotificationProductRepository;
import com.back.notification.adapter.out.NotificationUserRepository;
import com.back.notification.adapter.out.PriceAlertRepository;
import com.back.notification.domain.NotificationProduct;
import com.back.notification.domain.NotificationUser;
import com.back.notification.domain.PriceAlert;
import com.back.notification.dto.request.PriceAlertSaveRequestDto;
import com.back.notification.exception.NotificationProductNotFoundException;
import com.back.notification.exception.NotificationUserNotFoundException;
import com.back.notification.mapper.PriceAlertMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceAlertSaveUsecase {
    private final PriceAlertRepository priceAlertRepository;
    private final NotificationUserRepository notificationUserRepository;
    private final NotificationProductRepository notificationProductRepository;

    private final PriceAlertMapper priceAlertMapper;


    public void save(PriceAlertSaveRequestDto dto, Long userId) {
        NotificationUser user = notificationUserRepository.findById(userId)
                .orElseThrow(NotificationUserNotFoundException::new);

        NotificationProduct product = notificationProductRepository.findById(dto.productId())
                .orElseThrow(NotificationProductNotFoundException::new);

        PriceAlert priceAlert = priceAlertMapper.toPriceAlert(dto, user, product);

        priceAlertRepository.save(priceAlert);
    }
}
