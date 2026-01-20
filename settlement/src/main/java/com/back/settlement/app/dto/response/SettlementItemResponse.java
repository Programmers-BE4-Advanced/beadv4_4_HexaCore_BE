package com.back.settlement.app.dto.response;

import com.back.settlement.domain.SettlementEventType;
import com.back.settlement.domain.SettlementItemStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SettlementItemResponse(
        Long settlementItemId,
        Long orderId,
        Long productId,
        Long payerId,
        Long payeeId,
        String sellerName,
        SettlementEventType eventType,
        SettlementItemStatus status,
        BigDecimal amount,
        LocalDateTime confirmedAt
) {
}
