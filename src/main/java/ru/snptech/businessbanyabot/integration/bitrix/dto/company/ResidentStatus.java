package ru.snptech.businessbanyabot.integration.bitrix.dto.company;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;
import ru.snptech.businessbanyabot.model.user.UserRole;

@Getter
public enum ResidentStatus implements LabeledEnum {
    YES("281042", "да"),
    NO("281044", "нет"),
    EX_RESIDENT("281222", "Бывший резидент");

    private final String id;
    private final String label;

    ResidentStatus(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public UserRole toUserRole() {
        return switch (this) {
            case YES -> UserRole.RESIDENT;
            case NO, EX_RESIDENT -> UserRole.NON_RESIDENT;
        };
    }
}

