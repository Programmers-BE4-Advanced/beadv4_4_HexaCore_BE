package com.back.cash.app;

import com.back.cash.adapter.out.market.MarketPaymentsClient;
import com.back.cash.dto.request.PayAndHoldRequestDto;
import com.back.cash.dto.request.PaymentFailedRequestDto;
import com.back.cash.dto.request.TossConfirmRequest;
import com.back.cash.dto.request.TossFailRequestDto;
import com.back.cash.dto.response.ConfirmResultResponseDto;
import com.back.cash.dto.response.PayAndHoldResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class CashFacade {

    private final PayAndHoldUseCase payAndHoldUseCase;
    private final ConfirmTossPaymentUseCase confirmTossPaymentUseCase;
    private final MarketPaymentsClient marketPaymentsClient;
    private final FailTossPaymentUseCase failTossPaymentUseCase;

    @Transactional
    public PayAndHoldResponseDto payAndHold(PayAndHoldRequestDto dto) {
        return payAndHoldUseCase.execute(dto);
    }

    @Transactional
    public ConfirmResultResponseDto confirmTossPayment(TossConfirmRequest req) {
        ConfirmResultResponseDto result = confirmTossPaymentUseCase.execute(req);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        if (result.isSuccess()) {
                            marketPaymentsClient.notifyPaymentCompleted(result.completedDto());
                        } else {
                            marketPaymentsClient.notifyPaymentFailed(result.failedDto());
                        }
                    }
                }
        );
        return result;
    }

    @Transactional
    public void failTossPayment(TossFailRequestDto req) {
        PaymentFailedRequestDto failedDto = failTossPaymentUseCase.execute(req);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                marketPaymentsClient.notifyPaymentFailed(failedDto);
            }
        });
    }
}
