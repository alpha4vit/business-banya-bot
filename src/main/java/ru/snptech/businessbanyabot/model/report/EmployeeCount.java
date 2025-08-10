package ru.snptech.businessbanyabot.model.report;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

@Getter
public enum EmployeeCount implements LabeledEnum {
    UP_TO_50("0", "До 50", 0, 49),
    FROM_50_TO_100("1", "От 50 до 100", 50, 100),
    FROM_101_TO_500("2", "От 101 до 500", 101, 500),
    MORE_THAN_500("3", "Более 500", 501, Integer.MAX_VALUE);

    private final String id;
    private final String label;
    private final int min;
    private final int max;

    EmployeeCount(String id, String label, int min, int max) {
        this.id = id;
        this.label = label;
        this.min = min;
        this.max = max;
    }
}
