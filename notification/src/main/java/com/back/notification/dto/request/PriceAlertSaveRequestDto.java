package com.back.notification.dto.request;

import java.math.BigDecimal;

public record PriceAlertSaveRequestDto(
        BigDecimal targetPrice,
        Long productId
) {
}
