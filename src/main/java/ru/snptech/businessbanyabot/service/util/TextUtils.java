package ru.snptech.businessbanyabot.service.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TextUtils {

    public static String balanceToHumanReadable(String balance) {
        if (balance.isBlank()) {
            return "0";
        }

        return balance.replace("|", " ");
    }

    public static String pluralizeDays(long days){
        return pluralize(days, "день", "дня", "дней");
    }

    public static String pluralizeMoths(long months){
        return pluralize(months, "месяц", "месяца", "месяцев");
    }

    public static String pluralize(long number, String one, String few, String many) {
        long n = Math.abs(number) % 100;
        long n1 = n % 10;

        if (n > 10 && n < 20) {
            return number + " " + many;
        }
        if (n1 > 1 && n1 < 5) {
            return number + " " + few;
        }
        if (n1 == 1) {
            return number + " " + one;
        }
        return number + " " + many;
    }
}