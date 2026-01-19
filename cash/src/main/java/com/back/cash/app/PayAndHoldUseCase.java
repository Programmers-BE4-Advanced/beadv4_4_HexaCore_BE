package com.back.cash.app;

import com.back.cash.domain.Payment;
import com.back.cash.domain.Wallet;
import com.back.cash.domain.enums.PaymentStatus;
import com.back.cash.dto.request.PayAndHoldRequestDto;
import com.back.cash.dto.response.PayAndHoldResponseDto;
import com.back.cash.mapper.PaymentMapper;
import com.back.cash.adapter.out.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayAndHoldUseCase {
    private final WalletSupport walletSupport;
    private final CashLogSupport cashLogSupport;
    private final PaymentRepository paymentRepository;

    public PayAndHoldResponseDto execute(PayAndHoldRequestDto dto) {

        Payment payment = paymentRepository.findByRelTypeAndRelId(dto.relType(), dto.relId())
                .orElseGet(() -> paymentRepository.save(PaymentMapper.toPayment(dto)));

        if (payment.getStatus() == PaymentStatus.DONE) {
            return PaymentMapper.toPayAndHoldResponseDto(payment);
        }

        Wallet buyerWallet = walletSupport.getUserWallet(dto.buyerId());
        Wallet systemWallet = walletSupport.getSystemWallet();

        buyerWallet.withdraw(payment.getTotalAmount());
        systemWallet.deposit(payment.getTotalAmount());

        cashLogSupport.recordHoldingLog(
                buyerWallet,
                systemWallet,
                dto.totalAmount(),
                dto.relType(),
                dto.relId()
        );

        payment.markAsDone();

        return PaymentMapper.toPayAndHoldResponseDto(payment);
    }
}
