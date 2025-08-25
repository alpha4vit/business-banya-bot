package ru.snptech.businessbanyabot.model.payment;

import lombok.Getter;
import ru.snptech.businessbanyabot.model.report.Exportable;

@Getter
public enum PaymentStatus implements Exportable {
    PENDING("Ожидает оплаты"),
    PAID("Оплачено"),
    CANCELED("Отменена");

    @Override
    public String toHumanReadable() {
        return humanReadableValue;
    }

    private final String humanReadableValue;

    PaymentStatus(String humanReadableValue) {
        this.humanReadableValue = humanReadableValue;
    }
}
