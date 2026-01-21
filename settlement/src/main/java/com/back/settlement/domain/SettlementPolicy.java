package com.back.settlement.domain;

import java.math.BigDecimal;

public final class SettlementPolicy {
    public static final int CHUNK_SIZE = 100;
    public static final BigDecimal PLATFORM_FEE_RATE = new BigDecimal("0.10");
}
