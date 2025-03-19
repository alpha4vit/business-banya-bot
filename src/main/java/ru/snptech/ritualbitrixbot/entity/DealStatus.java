package ru.snptech.ritualbitrixbot.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DealStatus {
    SUCCESS("Успех"), FAILURE("Провал"), PROBLEM("Проблема");
    private final String lexem;
}
