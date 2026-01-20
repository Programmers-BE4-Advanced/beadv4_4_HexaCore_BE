package com.back.settlement.adapter.out;

import com.back.settlement.domain.SettlementItem;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SettlementItemRepository extends JpaRepository<SettlementItem, Long> {
    boolean existsByOrderId(Long orderId);

    @Query("SELECT DISTINCT si.payeeId FROM SettlementItem si " +
            "WHERE si.settlement IS NULL " +
            "AND si.payeeId IS NOT NULL " +
            "AND si.confirmedAt >= :startAt AND si.confirmedAt <= :endAt")
    List<Long> findDistinctPayeeIdBySettlementIsNullAndConfirmedAtBetween(LocalDateTime startAt, LocalDateTime endAt);

    List<SettlementItem> findByPayeeIdAndSettlementIsNullAndConfirmedAtBetween(Long payeeId, LocalDateTime startAt, LocalDateTime endAt);
}
