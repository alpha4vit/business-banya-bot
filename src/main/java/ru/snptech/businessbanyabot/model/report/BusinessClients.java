package ru.snptech.businessbanyabot.model.report;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

@Getter
public enum BusinessClients implements LabeledEnum {
    SMALL_BUSINESS("281598", "Малый бизнес"),
    MEDIUM_BUSINESS("281600", "Средний бизнес"),
    LARGE_BUSINESS("281602", "Крупный бизнес"),
    B2C("281604", "Частные лица (B2C)"),
    NGO("281606", "НКО"),
    INDIVIDUALS("281608", "Физ.лица");

    private final String id;
    private final String label;

    BusinessClients(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
