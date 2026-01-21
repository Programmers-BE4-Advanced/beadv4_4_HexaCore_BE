package com.back.settlement.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SettlementEventType {
    SETTLEMENT_PRODUCT_SALES_AMOUNT("상품판매_대금"),
    SETTLEMENT_PRODUCT_SALES_FEE("상품판매_수수료");

    private final String value;
}
