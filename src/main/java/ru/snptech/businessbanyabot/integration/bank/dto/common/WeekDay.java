package ru.snptech.businessbanyabot.integration.bank.dto.common;

import lombok.Getter;

@Getter
public enum WeekDay {

    MONDAY("Понедельник"),
    TUESDAY("Вторник"),
    WEDNESDAY("Среда"),
    THURSDAY("Четверг"),
    FRIDAY("Пятница"),
    SATURDAY("Суббота"),
    SUNDAY("Воскресенье");

    private final String value;

    WeekDay(String value) {
        this.value = value;
    }

    public static WeekDay fromValue(String value) {
        if (value == null || value.isBlank()) return null;

        for (WeekDay day : WeekDay.values()) {
            if (day.getValue().equalsIgnoreCase(value)) {
                return day;
            }
        }

        throw new IllegalArgumentException("Неизвестное значение дня недели: " + value);
    }
}
