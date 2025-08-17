package ru.snptech.businessbanyabot.model.event;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

@Getter
public enum EventType implements LabeledEnum {
    CLUBS("281792", "Клубы"),
    BATH("281794", "Бани"),
    WITH_SPEAKER("281796", "Мероприятия со спикером"),
    NOT_DEFINED("1", "Все");

    private final String id;
    private final String label;

    EventType(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
