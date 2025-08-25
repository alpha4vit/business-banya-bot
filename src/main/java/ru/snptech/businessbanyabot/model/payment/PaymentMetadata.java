package ru.snptech.businessbanyabot.model.payment;

public record PaymentMetadata(
    Integer paymentAmount,
    Integer subscriptionDurationInMonths
) {
}
