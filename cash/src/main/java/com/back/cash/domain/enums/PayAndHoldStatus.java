package com.back.cash.domain.enums;

public enum PayAndHoldStatus {
    PAID,         // 예치금만으로 즉시 결제 완료(홀딩까지 완료)
    REQUIRES_PG   // 부족분 PG 결제 필요
}
