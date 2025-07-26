package ru.snptech.businessbanyabot.integrations.bank.dto.response;

public record RegisterPaymentQrCodeResponseData(
    String status,
    String payload,
    String accountId,
    String createdAt,
    String merchantId,
    String legalId,
    String qrcId,
    Integer amount,
    String ttl,
    String paymentPurpose,
    QrImage image,
    Integer commissionPercent,
    String currency,
    String qrcType,
    String templateVersion,
    String sourceName
) {
}
