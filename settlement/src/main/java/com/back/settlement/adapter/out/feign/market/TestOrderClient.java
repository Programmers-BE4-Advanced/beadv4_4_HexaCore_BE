package com.back.settlement.adapter.out.feign.market;

import com.back.settlement.app.event.SettlementItemRequest;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("local")
public class TestOrderClient implements OrderClient {
    private final List<SettlementItemRequest> orders = new ArrayList<>();

    @Override
    public List<SettlementItemRequest> findSettlementTargetOrders(YearMonth targetMonth, int page, int size) {
        log.info("[TestOrderClient] 정산 대상 주문 조회. targetMonth={}, page={}, size={}", targetMonth, page, size);
        LocalDate startDate = targetMonth.atDay(1);
        LocalDate endDate = targetMonth.atEndOfMonth();
        List<SettlementItemRequest> filtered = orders.stream()
                .filter(order -> {
                    LocalDate orderDate = order.confirmedAt().toLocalDate();
                    return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                })
                .toList();
        int fromIndex = page * size;
        if (fromIndex >= filtered.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + size, filtered.size());

        List<SettlementItemRequest> result = filtered.subList(fromIndex, toIndex);
        log.info("[TestOrderClient] 조회된 주문 수: {}", result.size());
        return result;
    }

    public void addOrder(SettlementItemRequest order) {
        orders.add(order);
    }

    public void clear() {
        orders.clear();
    }
}