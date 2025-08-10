package ru.snptech.businessbanyabot.model.report;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

@Getter
public enum ChildrenCount implements LabeledEnum {
    NONE("0", "Отсутствуют", 0),
    ONE("1", "Один", 1),
    TWO("2", "Двое", 2),
    THREE("3", "Трое", 3),
    FOUR_AND_MORE("4", "Четверо и больше", 4);

    private final String id;
    private final String label;

    private final Integer count;

    ChildrenCount(String id, String label, Integer count) {
        this.id = id;
        this.label = label;
        this.count = count;
    }
}
