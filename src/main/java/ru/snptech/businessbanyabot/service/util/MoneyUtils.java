package ru.snptech.businessbanyabot.service.util;

import lombok.experimental.UtilityClass;
import ru.snptech.businessbanyabot.model.common.Money;

import java.util.Arrays;

@UtilityClass
public class MoneyUtils {

    private final String SEPARATOR = ",";

    public static String getHumanReadableAmount(Integer amount) {
        if (amount == null) return "0,00";

        var major = amount / 100L;
        var minor = amount % 100L;

        return String.format("%d%s%02d", major, SEPARATOR, minor);
    }

    public static String withCurrency(Integer amount, String currency) {
        var humanReadable = getHumanReadableAmount(amount);

       return String.format("%s %s".formatted(humanReadable, currency));
    }

    public static Money parse(String money) {
        if (money.isBlank()) {
            return null;
        }

        var parts = Arrays.stream(money.split("\\|")).map(String::trim).toList();

        return new Money(Integer.parseInt(parts.get(0)), parts.get(1));
    }
}
