package com.back.cash.adapter.out.market;

import com.back.cash.dto.request.PaymentCompletedRequestDto;
import com.back.cash.dto.request.PaymentFailedRequestDto;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/market/internal")
@Profile("!local")
public interface MarketPaymentsHttpClient extends MarketPaymentsClient {

    @Override
    @PostExchange("/payment-confirm")
    void notifyPaymentCompleted(@RequestBody PaymentCompletedRequestDto dto);

    @Override
    @PostExchange("/payment-failed")
    void notifyPaymentFailed(@RequestBody PaymentFailedRequestDto dto);
}
