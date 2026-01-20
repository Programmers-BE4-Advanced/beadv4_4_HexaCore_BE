package com.back.settlement.app.support;

import com.back.common.code.FailureCode;
import com.back.common.exception.EntityNotFoundException;
import com.back.settlement.adapter.out.SettlementItemRepository;
import com.back.settlement.adapter.out.SettlementRepository;
import com.back.settlement.domain.Settlement;
import com.back.settlement.domain.SettlementItem;
import com.back.settlement.domain.SettlementStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 정산 관련 조회를 담당하는 Support 클래스입니다.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementSupport {
    private final SettlementRepository settlementRepository;
    private final SettlementItemRepository settlementItemRepository;

    public List<Settlement> findBySellerId(Long sellerId) {
        return settlementRepository.findBySellerId(sellerId);
    }

    public SettlementItem findSettlementItemById(Long settlementItemId) {
        return settlementItemRepository.findById(settlementItemId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.SETTLEMENT_ITEM_NOT_FOUND));
    }

    public boolean existsByOrderId(Long orderId) {
        return settlementItemRepository.existsByOrderId(orderId);
    }

    public List<Long> findUnsettledPayeeIds(LocalDateTime startAt, LocalDateTime endAt) {
        return settlementItemRepository.findDistinctPayeeIdBySettlementIsNullAndConfirmedAtBetween(startAt, endAt);
    }

    public List<SettlementItem> findUnsettledItemsByPayeeId(Long payeeId, LocalDateTime startAt, LocalDateTime endAt) {
        return settlementItemRepository.findByPayeeIdAndSettlementIsNullAndConfirmedAtBetween(payeeId, startAt, endAt);
    }

    public List<Settlement> findPendingSettlements(Pageable pageable) {
        return settlementRepository.findByStatus(SettlementStatus.PENDING, pageable);
    }
}