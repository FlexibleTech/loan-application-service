package io.github.flexibletech.offering;

import java.time.LocalDate;

public class LocalDateUtils {
    private LocalDateUtils() {
    }

    public static LocalDate currentLocalDateMinusDays(int days) {
        return LocalDate.now().minusDays(days);
    }

}
