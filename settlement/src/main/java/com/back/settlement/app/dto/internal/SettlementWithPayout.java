package com.back.settlement.app.dto.internal;

import com.back.settlement.app.event.SettlementPayoutRequest;
import com.back.settlement.domain.Settlement;

public record SettlementWithPayout(
        Settlement settlement,
        SettlementPayoutRequest payoutRequest
) {
}
