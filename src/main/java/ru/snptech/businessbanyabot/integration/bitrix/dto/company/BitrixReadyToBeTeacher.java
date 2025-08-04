package ru.snptech.businessbanyabot.integration.bitrix.dto.company;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

@Getter
public enum BitrixReadyToBeTeacher implements LabeledEnum {
    YES("281648", "Да"),
    NO("281650", "Нет");

    private final String id;
    private final String label;

    BitrixReadyToBeTeacher(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
