package ru.snptech.businessbanyabot.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DealFlow {
    SHOP("Магазин"), FUNERAL("Похороны");
    private final String lexem;
}
