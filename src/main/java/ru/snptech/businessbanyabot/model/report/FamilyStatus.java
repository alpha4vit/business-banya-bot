package ru.snptech.businessbanyabot.model.report;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

@Getter
public enum FamilyStatus implements LabeledEnum {
    MARRIED("281564", "Женат"),
    SINGLE("281566", "Холост"),
    DIVORCED("281568", "В разводе"),
    IN_RELATIONSHIP("281570", "В отношениях");

    private final String id;
    private final String label;

    FamilyStatus(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
