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

}
