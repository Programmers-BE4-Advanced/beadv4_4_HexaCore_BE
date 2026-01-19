package com.back.common.market.event;

import java.math.BigDecimal;

public record SellBiddingCreatedEvent(
        Long productId,
        BigDecimal currentPrice
) {
}
