package ru.snptech.businessbanyabot.integrations.bank.dto.common;

import lombok.Getter;

@Getter
public enum QrType {
    STATIC("01"),
    DYNAMIC("02");

    private final String value;

    QrType(String value) {
        this.value = value;
    }
}
