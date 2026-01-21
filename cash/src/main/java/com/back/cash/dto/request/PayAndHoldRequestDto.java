package com.back.cash.dto.request;

import com.back.cash.domain.enums.RelType;

import java.math.BigDecimal;

public record PayAndHoldRequestDto(
        RelType relType,
        Long relId,
        Long buyerId,           // 결제 요청자 ID
        BigDecimal totalAmount, // 결제 총 금액
        String orderName       // PG 결제창에 노출될 상품명
) {
}
