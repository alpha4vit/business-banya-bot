package ru.snptech.businessbanyabot.integrations.bank.dto.request;

public record RegisterPaymentQrCodeRequest(
    Integer amount,
    String currency,
    String paymentPurpose,
    String qrcType,
    QRCodeRequestParams imageParams,
    String sourceName,
    Integer ttl
) {
}
