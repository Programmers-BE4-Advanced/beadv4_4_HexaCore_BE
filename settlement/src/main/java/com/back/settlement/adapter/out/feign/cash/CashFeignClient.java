package com.back.settlement.adapter.out.feign.cash;

import com.back.settlement.app.event.SettlementPayoutRequest;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
@Profile("!test & !local")
public interface CashFeignClient extends CashClient {

    @Override
    @PostExchange("/api/v1/cash/payout")
    void requestPayout(@RequestBody List<SettlementPayoutRequest> payoutRequests);
}
