package ru.snptech.businessbanyabot.integration.bitrix.dto.company;

import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

public enum BitrixGrowthLimit implements LabeledEnum {
    FINANCE("281634", "Финансы (недостаток инвестиций/оборотных средств)"),
    TEAM("281636", "Команда (кадровый голод, низкая квалификация)"),
    MARKET("281638", "Рынок (малый спрос, высокая конкуренция)"),
    TIME("281640", "Время (нехватка ресурсов на управление)"),
    REGULATORY("281642", "Регуляторные барьеры (налоги, законы)"),
    EXPERIENCE("281644", "Недостаток опыта"),
    OTHER("281646", "Другое");

    private final String id;
    private final String label;

    BitrixGrowthLimit(String id, String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
