package com.back.settlement.app.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SettlementRequest(
        Long sellerId,
        String sellerName,
        LocalDateTime startAt,
        LocalDateTime endAt,
        BigDecimal totalSalesAmount,
        BigDecimal totalFeeAmount,
        BigDecimal totalNetAmount
) {
}
