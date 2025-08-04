package ru.snptech.businessbanyabot.service.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MoneyUtils {

    private final String SEPARATOR = ",";

    public static String getHumanReadableAmount(Integer amount) {
        if (amount == null) return "0,00";

        int major = amount / 100;
        int minor = amount % 100;

        return String.format("%d%s%02d", major, SEPARATOR, minor);
    }
}
