package com.back.cash.dto.response;

import com.back.cash.domain.enums.PayAndHoldStatus;
import com.back.cash.domain.enums.RelType;

import java.math.BigDecimal;

public record PayAndHoldResponseDto(
        PayAndHoldStatus status,

        RelType relType,
        Long relId,

        BigDecimal walletUsedAmount,   // 예치금으로 즉시 사용/홀딩된 금액
        BigDecimal pgRequiredAmount,   // PG로 결제해야 하는 부족분(0이면 PAID)

        // REQUIRES_PG에만 생성/반환되는 토스 orderId(UUID). PAID면 null.
        String tossOrderId              // 토스 orderId로 사용할 UUID
) {}
