package ru.snptech.businessbanyabot.integration.bitrix.dto.company;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

@Getter
public enum BitrixSport implements LabeledEnum {
    FOOTBALL("281572", "Футбол"),
    HOCKEY("281596", "Хоккей"),
    VOLLEYBALL("281594", "Волейбол"),
    SWIMMING("281574", "Плавание"),
    MARTIAL_ARTS("281588", "Единоборства"),
    GYM_FITNESS("281584", "Тренажерный зал/фитнес"),
    RUNNING("281580", "Бег/легкая атлетика"),
    CYCLING("281582", "Велоспорт"),
    TENNIS("281576", "Теннис"),
    GOLF("281578", "Гольф"),
    YOGA("281586", "Йога"),
    SKI_SNOWBOARD("281592", "Лыжи/Сноуборд"),
    OTHER("281590", "Другое");;

    private final String id;
    private final String label;

    BitrixSport(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
