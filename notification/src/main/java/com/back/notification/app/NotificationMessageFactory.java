package com.back.notification.app;

import com.back.notification.domain.Notification;
import com.back.notification.dto.NotificationMessage;
import com.back.notification.domain.TemplateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationMessageFactory {

    private final MessageSource messageSource;

    public NotificationMessage create(Notification notification, Locale locale) {
        TemplateSpec spec = extract(notification);

        String title = messageSource.getMessage(
                spec.templateKey() + ".title",
                spec.args(),
                locale
        );

        String body = messageSource.getMessage(
                spec.templateKey() + ".body",
                spec.args(),
                locale
        );

        return new NotificationMessage(title, body);
    }

    private TemplateSpec extract(Notification notification) {
        Map<String, Object> c = notification.getContent();

        return switch (notification.getType()) {
            case BID_COMPLETED -> new TemplateSpec(
                    "notification.bid.completed",
                    new Object[]{
                            c.get("productName"),
                            c.get("productSize"),
                            c.get("price")
                    }
            );

            case BID_FAILED -> new TemplateSpec(
                    "notification.bid.failed",
                    new Object[]{
                            c.get("productName"),
                            c.get("productSize"),
                            c.get("price")
                    }
            );

            case PURCHASE_CANCELED -> new TemplateSpec(
                    "notification.purchase.canceled",
                    new Object[]{
                            c.get("productName"),
                            c.get("productSize")
                    }
            );

            case INSPECTION_COMPLETED -> new TemplateSpec(
                    "notification.inspection.completed",
                    new Object[]{
                            c.get("productName"),
                            c.get("productSize")
                    }
            );

            case PRICE_DROPPED -> new TemplateSpec(
                    "notification.price.dropped",
                    new Object[]{
                            c.get("productName"),
                            c.get("productSize"),
                            c.get("targetPrice")
                    }
            );

            case SETTLEMENT_COMPLETED -> new TemplateSpec(
                    "notification.settlement.completed",
                    new Object[]{
                            10, // 정산 월 (임시 하드코딩, 추후 이벤트 값으로 교체 예정)
                            c.get("endAt"),
                            c.get("totalNetAmount")
                    }
            );
        };
    }
}
