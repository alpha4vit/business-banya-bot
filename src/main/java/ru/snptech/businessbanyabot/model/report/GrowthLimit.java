package ru.snptech.businessbanyabot.model.report;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

@Getter
public enum GrowthLimit implements LabeledEnum {
    FINANCES("281634", "Финансы (недостаток инвестиций/оборотных средств)"),
    TEAM("281636", "Команда (кадровый голод, низкая квалификация)"),
    MARKET("281638", "Рынок (малый спрос, высокая конкуренция)"),
    TIME("281640", "Время (нехватка ресурсов на управление)"),
    REGULATORY_BARRIERS("281642", "Регуляторные барьеры (налоги, законы)"),
    LACK_OF_EXPERIENCE("281644", "Недостаток опыта"),
    OTHER("281646", "Другое");

    private final String id;
    private final String label;

    GrowthLimit(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
