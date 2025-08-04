package ru.snptech.businessbanyabot.integration.bitrix.dto.company;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

@Getter
public enum BitrixNetworkingSphere implements LabeledEnum {
    FINANCE("281622", "Финансы и инвестиции"),
    IT("281624", "IT и цифровые технологии"),
    MANUFACTURING("281626", "Производство и логистика"),
    MARKETING("281628", "Маркетинг и реклама"),
    LAW("281630", "Юриспруденция"),
    OTHER("281632", "Другое");

    private final String id;
    private final String label;

    BitrixNetworkingSphere(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
