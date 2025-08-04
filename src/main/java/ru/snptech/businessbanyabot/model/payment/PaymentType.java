package ru.snptech.businessbanyabot.model.payment;

import lombok.Getter;
import ru.snptech.businessbanyabot.model.report.Exportable;

@Getter
public enum PaymentType implements Exportable {
    FAST_PAYMENT("Быстрый платеж"),
    INVOICE("Счет"),
    DEPOSIT_FAST_PAYMENT("Пополнение, быстрый платеж"),
    DEPOSIT_INVOICE("Пополнение, выставление счета");

    @Override
    public String toHumanReadable() {
        return null;
    }

    private final String humanReadableValue;

    PaymentType(String humanReadableValue) {
        this.humanReadableValue = humanReadableValue;
    }
}
