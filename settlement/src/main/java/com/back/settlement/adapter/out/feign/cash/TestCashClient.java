package com.back.settlement.adapter.out.feign.cash;

import com.back.settlement.app.event.SettlementPayoutRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("local")
public class TestCashClient implements CashClient {

    private final List<SettlementPayoutRequest> payoutHistory = new ArrayList<>();

    @Override
    public void requestPayout(List<SettlementPayoutRequest> payoutRequests) {
        log.info("[TestCashClient] 캐시 지급 요청. 건수={}", payoutRequests.size());
        payoutRequests.forEach(request ->
                log.info("[TestCashClient] 지급 요청 - settlementId={}, payeeId={}, amount={}",
                        request.settlementId(), request.payeeId(), request.amount()));
        payoutHistory.addAll(payoutRequests);
    }

    public List<SettlementPayoutRequest> getPayoutHistory() {
        return List.copyOf(payoutHistory);
    }

    public void clear() {
        payoutHistory.clear();
    }
}
