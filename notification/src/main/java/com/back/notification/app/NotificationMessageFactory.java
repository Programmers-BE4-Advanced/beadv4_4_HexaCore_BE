package com.back.notification.app;

import com.back.notification.domain.Notification;
import com.back.notification.dto.NotificationMessage;
import com.back.notification.domain.TemplateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
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

            case SETTLEMENT_COMPLETED -> {
                LocalDate startDate = ((LocalDateTime) c.get("startAt")).toLocalDate();
                LocalDate endDate = ((LocalDateTime) c.get("endAt")).toLocalDate();
                YearMonth settlementMonth = YearMonth.from(startDate);

                yield new TemplateSpec(
                        "notification.settlement.completed",
                        new Object[]{
                                settlementMonth.getMonthValue(),
                                java.sql.Date.valueOf(startDate),
                                java.sql.Date.valueOf(endDate),
                                c.get("totalNetAmount")
                        }
                );
            }

        };
    }
}
