package ru.snptech.businessbanyabot.model.payment;

import lombok.Getter;
import ru.snptech.businessbanyabot.model.report.Exportable;

@Getter
public enum PaymentStatus implements Exportable {
    PENDING("Ожидает оплаты"),
    PAID("Оплачено");

    @Override
    public String toHumanReadable() {
        return null;
    }

    private final String humanReadableValue;

    PaymentStatus(String humanReadableValue) {
        this.humanReadableValue = humanReadableValue;
    }
}
