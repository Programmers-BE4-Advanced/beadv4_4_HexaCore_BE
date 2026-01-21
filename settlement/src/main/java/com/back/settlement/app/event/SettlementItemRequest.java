package com.back.settlement.app.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Market 모듈 → Settlement 모듈로 전달되는 데이터
public record SettlementItemRequest(
        Long orderId,
        Long productId,
        Long buyerId,
        Long sellerId,
        String sellerName,
        BigDecimal price,
        LocalDateTime confirmedAt
) {
}
