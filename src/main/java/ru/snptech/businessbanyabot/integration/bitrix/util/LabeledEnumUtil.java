package ru.snptech.businessbanyabot.integration.bitrix.util;

import lombok.experimental.UtilityClass;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;
import ru.snptech.businessbanyabot.model.report.ReportType;

@UtilityClass
public final class LabeledEnumUtil {
    public static <T extends Enum<T> & LabeledEnum> T fromId(Class<T> enumClass, String id) {
        for (T constant : enumClass.getEnumConstants()) {
            if (constant.getId().equals(id)) {
                return constant;
            }
        }

        throw new IllegalArgumentException("Cannot parse phoneId %s for %s".formatted(id, enumClass.getName()));
    }

    public static <T extends Enum<T> & LabeledEnum> T fromLabel(Class<T> enumClass, String label) {
        for (T constant : enumClass.getEnumConstants()) {
            if (constant.getLabel().equalsIgnoreCase(label)) {
                return constant;
            }
        }

        throw new IllegalArgumentException("Cannot parse label %s for %s".formatted(label, enumClass.getName()));
    }
}
