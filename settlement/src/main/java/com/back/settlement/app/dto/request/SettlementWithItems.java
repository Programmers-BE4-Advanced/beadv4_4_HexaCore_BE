package com.back.settlement.app.dto.request;

import com.back.settlement.domain.Settlement;
import com.back.settlement.domain.SettlementItem;
import java.util.List;

public record SettlementWithItems(
        Settlement settlement,
        List<SettlementItem> items
) {
}
