package com.back.cash.mapper;

import com.back.cash.domain.Payment;
import com.back.cash.domain.enums.PayAndHoldStatus;
import com.back.cash.domain.enums.PaymentStatus;
import com.back.cash.dto.request.PayAndHoldRequestDto;
import com.back.cash.dto.response.PayAndHoldResponseDto;

import java.math.BigDecimal;

public class  PaymentMapper {

    public static Payment toPayment(PayAndHoldRequestDto dto) {
        return Payment.builder()
                .userId(dto.buyerId())
                .relType(dto.relType())
                .relId(dto.relId())
                .totalAmount(dto.totalAmount())
                .walletUsedAmount(dto.totalAmount())
                .pgAmount(BigDecimal.ZERO)
                .status(PaymentStatus.READY)
                .build();
    }

    public static PayAndHoldResponseDto toPayAndHoldResponseDto(Payment payment) {
        return new PayAndHoldResponseDto(
                PayAndHoldStatus.PAID,
                payment.getRelType(),
                payment.getRelId(),
                payment.getWalletUsedAmount(),
                BigDecimal.ZERO,
                null
        );
    }

}
