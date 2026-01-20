package com.back.settlement.batch;

import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettlementScheduler {
    private final SettlementJobLauncher settlementJobLauncher;

    @Scheduled(cron = "0 0 2 1 * *")  // 매월 1일 02:00 실행
    public void runMonthlySettlement() {
        YearMonth targetMonth = YearMonth.now().minusMonths(1);
        log.info("월간 정산 스케줄 실행. targetMonth={}", targetMonth);
        try {
            settlementJobLauncher.run(targetMonth);
        } catch (Exception e) {
            // 예외 발생 시 로그만 남기고 종료
            // TODO: 운영 단계에서 알림을 Slack 전송
            log.error("월간 정산 스케줄 실행 실패. targetMonth={}", targetMonth, e);
        }
    }
}