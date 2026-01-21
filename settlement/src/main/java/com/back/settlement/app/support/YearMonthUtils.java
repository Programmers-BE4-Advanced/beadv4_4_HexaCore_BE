package com.back.settlement.app.support;

import java.time.LocalDateTime;
import java.time.YearMonth;

public final class YearMonthUtils {
    public static YearMonth parseOrDefault(String str) {
        if (str == null) {
            return YearMonth.now().minusMonths(1);
        }
        return YearMonth.parse(str);
    }

    public static LocalDateTime startOfMonth(YearMonth yearMonth) {
        return yearMonth.atDay(1).atStartOfDay();
    }

    public static LocalDateTime endOfMonth(YearMonth yearMonth) {
        return yearMonth.atEndOfMonth().atTime(23, 59, 59);
    }
}
