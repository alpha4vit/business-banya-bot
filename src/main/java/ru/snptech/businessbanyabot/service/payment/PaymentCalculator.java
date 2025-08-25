package ru.snptech.businessbanyabot.service.payment;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class PaymentCalculator {

    public static Integer calculateExtensionCost(Instant residentUntil, int durationInMonths) {
        LocalDate startDate = residentUntil.atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
        var total = 0;

        for (int i = 0; i < durationInMonths; i++) {
            LocalDate month = startDate.plusMonths(i);

            if (month.getMonthValue() < 7) {
                total += 3000;
            } else {
                total += 5000;
            }
        }

        // minor units
        return total * 100;
    }
}