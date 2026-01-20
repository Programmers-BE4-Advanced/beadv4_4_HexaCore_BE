package com.back.settlement.adapter.out.feign.market;

import com.back.settlement.app.event.SettlementItemRequest;
import java.time.YearMonth;
import java.util.List;

public interface OrderClient {
    List<SettlementItemRequest> findSettlementTargetOrders(YearMonth targetMonth, int page, int size);
}