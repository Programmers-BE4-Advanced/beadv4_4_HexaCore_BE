package com.back.settlement.app.usecase;

import static com.back.settlement.domain.SettlementEventType.SETTLEMENT_PRODUCT_SALES_AMOUNT;
import static com.back.settlement.domain.SettlementEventType.SETTLEMENT_PRODUCT_SALES_FEE;
import static com.back.settlement.domain.SettlementPolicy.PLATFORM_FEE_RATE;

import com.back.settlement.adapter.out.SettlementItemRepository;
import com.back.settlement.adapter.out.SettlementRepository;
import com.back.settlement.app.dto.request.SettlementRequest;
import com.back.settlement.domain.Settlement;
import com.back.settlement.domain.SettlementItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementCreateUseCase {
    private final SettlementRepository settlementRepository;
    private final SettlementItemRepository settlementItemRepository;
    private static final String SYSTEM_NAME = "SYSTEM";

    public Settlement createSettlementForPayee(Long payeeId, List<SettlementItem> items, LocalDateTime startAt, LocalDateTime endAt) {
        SettlementAmounts amounts = calculateAmounts(items);
        String payeeName = extractPayeeName(items);
        SettlementRequest request = new SettlementRequest(
                payeeId,
                payeeName,
                startAt,
                endAt,
                amounts.totalSalesAmount(),
                amounts.totalFeeAmount(),
                amounts.totalNetAmount()
        );
        return Settlement.createSettlement(request);
    }

    @Transactional
    public Settlement saveSettlement(Settlement settlement) {
        return settlementRepository.save(settlement);
    }

    @Transactional
    public void saveSettlementWithItems(Settlement settlement, List<SettlementItem> items) {
        Settlement savedSettlement = settlementRepository.save(settlement);
        items.forEach(item -> item.addSettlement(savedSettlement));
        settlementItemRepository.saveAll(items);
    }

    private String extractPayeeName(List<SettlementItem> items) {
        return items.stream()
                .filter(item -> item.getEventType() == SETTLEMENT_PRODUCT_SALES_AMOUNT)
                .map(SettlementItem::getSellerName)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(SYSTEM_NAME);
    }

    private SettlementAmounts calculateAmounts(List<SettlementItem> items) {
        BigDecimal salesAmount = items.stream()
                .filter(item -> item.getEventType() == SETTLEMENT_PRODUCT_SALES_AMOUNT)
                .map(SettlementItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal feeRevenue = items.stream()
                .filter(item -> item.getEventType() == SETTLEMENT_PRODUCT_SALES_FEE)
                .map(SettlementItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (salesAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal feeAmount = salesAmount.multiply(PLATFORM_FEE_RATE).setScale(0, RoundingMode.HALF_UP);
            BigDecimal netAmount = salesAmount.subtract(feeAmount);
            return new SettlementAmounts(salesAmount, feeAmount, netAmount);
        }

        return new SettlementAmounts(feeRevenue, BigDecimal.ZERO, feeRevenue);
    }

    private record SettlementAmounts(BigDecimal totalSalesAmount, BigDecimal totalFeeAmount, BigDecimal totalNetAmount) {
    }
}
