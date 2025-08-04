package ru.snptech.businessbanyabot.service.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TextUtils {

    public static String balanceToHumanReadable(String balance) {
        return balance.replace("|", " ");
    }

}
