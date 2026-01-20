package com.back.settlement.app.facade;

import com.back.settlement.app.event.SettlementItemRequest;
import com.back.settlement.app.dto.response.SettlementItemResponse;
import com.back.settlement.app.dto.response.SettlementResponse;
import com.back.settlement.app.support.SettlementSupport;
import com.back.settlement.app.usecase.SettlementCompleteUseCase;
import com.back.settlement.app.usecase.SettlementCreateUseCase;
import com.back.settlement.app.usecase.SettlementItemAddUseCase;
import com.back.settlement.app.usecase.SettlementUseCase;
import com.back.settlement.app.dto.request.SettlementWithItems;
import com.back.settlement.app.dto.request.SettlementWithPayout;
import com.back.settlement.domain.Settlement;
import com.back.settlement.domain.SettlementItem;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementFacade {
    private final SettlementUseCase settlementUseCase;
    private final SettlementItemAddUseCase settlementItemAddUseCase;
    private final SettlementCreateUseCase settlementCreateUseCase;
    private final SettlementCompleteUseCase settlementCompleteUseCase;
    private final SettlementSupport settlementSupport;

    private record DateTimeRange(LocalDateTime startAt, LocalDateTime endAt) {
        private static DateTimeRange ofMonth(YearMonth yearMonth) {
            return new DateTimeRange(
                    yearMonth.atDay(1).atStartOfDay(),
                    yearMonth.atEndOfMonth().atTime(23, 59, 59)
            );
        }
    }

    public List<SettlementResponse> getSettlements(Long sellerId) {
        return settlementUseCase.getSettlements(sellerId);
    }

    public SettlementItemResponse getSettlementItem(Long settlementItemId, Long sellerId) {
        return settlementUseCase.getSettlementItem(settlementItemId, sellerId);
    }

    public void addSettlementItem(SettlementItemRequest request) {
        settlementItemAddUseCase.add(request);
    }

    public List<Long> findUnsettledPayeeIds(YearMonth targetMonth) {
        DateTimeRange range = DateTimeRange.ofMonth(targetMonth);
        return settlementSupport.findUnsettledPayeeIds(range.startAt(), range.endAt());
    }

    public SettlementWithItems createSettlementForPayee(Long payeeId, YearMonth targetMonth) {
        DateTimeRange range = DateTimeRange.ofMonth(targetMonth);
        List<SettlementItem> unsettledItems = findUnsettledItems(payeeId, range.startAt(), range.endAt());
        Settlement settlement = settlementCreateUseCase.createSettlementForPayee(payeeId, unsettledItems, range.startAt(), range.endAt());
        log.info("정산 생성 완료. payeeId={}, itemCount={}, netAmount={}", payeeId, unsettledItems.size(), settlement.getTotalNetAmount());
        return new SettlementWithItems(settlement, unsettledItems);
    }

    public void saveSettlement(SettlementWithItems settlementWithItems) {
        settlementCreateUseCase.saveSettlementWithItems(
                settlementWithItems.settlement(),
                settlementWithItems.items()
        );
    }

    private List<SettlementItem> findUnsettledItems(Long payeeId, LocalDateTime startAt, LocalDateTime endAt) {
        List<SettlementItem> items = settlementSupport.findUnsettledItemsByPayeeId(payeeId, startAt, endAt);
        if (items.isEmpty()) {
            log.debug("{}의 미정산 항목이 없습니다.", payeeId);
        }
        return items;
    }

    public List<Settlement> findPendingSettlements(Pageable pageable) {
        return settlementSupport.findPendingSettlements(pageable);
    }

    public SettlementWithPayout completeSettlement(Settlement settlement) {
        return settlementCompleteUseCase.completeSettlement(settlement);
    }

    public void saveAndRequestPayout(List<SettlementWithPayout> settlementWithPayouts) {
        settlementCompleteUseCase.saveAndRequestPayout(settlementWithPayouts);
    }
}
