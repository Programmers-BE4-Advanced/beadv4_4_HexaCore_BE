package com.back.notification.app;

import com.back.common.market.event.SellBiddingCreatedEvent;
import com.back.notification.adapter.out.PriceAlertRepository;
import com.back.notification.domain.NotificationUser;
import com.back.notification.domain.PriceAlert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PriceAlertSupport {
    private final PriceAlertRepository priceAlertRepository;

    public List<NotificationUser> findUsersForPriceDropAlert(SellBiddingCreatedEvent event) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(1);

        List<PriceAlert> priceAlerts = priceAlertRepository.findEligibleAlerts(event.productId(),
                event.currentPrice(), threshold);
        return priceAlerts.stream()
                .map(PriceAlert::getUser)
                .distinct()
                .toList();
    }
}
