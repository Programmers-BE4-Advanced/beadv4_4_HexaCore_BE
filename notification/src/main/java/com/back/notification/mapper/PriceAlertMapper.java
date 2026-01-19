package com.back.notification.mapper;

import com.back.notification.domain.NotificationProduct;
import com.back.notification.domain.NotificationUser;
import com.back.notification.domain.PriceAlert;
import com.back.notification.dto.request.PriceAlertSaveRequestDto;
import com.back.notification.dto.response.PriceAlertIdDto;
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

    public PriceAlertIdDto toPriceAlertIdDto(Long id) {
        return PriceAlertIdDto.builder()
                .id(id)
                .build();
    }
}
