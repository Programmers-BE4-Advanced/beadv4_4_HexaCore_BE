package com.back.notification.mapper;

import com.back.notification.domain.NotificationProduct;
import com.back.notification.domain.NotificationUser;
import com.back.notification.domain.PriceAlert;
import com.back.notification.dto.request.PriceAlertSaveRequestDto;
import org.springframework.stereotype.Component;

@Component
public class PriceAlertMapper {
    public PriceAlert toPriceAlert(PriceAlertSaveRequestDto dto, NotificationUser user,
                                   NotificationProduct product) {
        return PriceAlert.builder()
                .user(user)
                .product(product)
                .targetPrice(dto.targetPrice())
                .build();
    }
}
