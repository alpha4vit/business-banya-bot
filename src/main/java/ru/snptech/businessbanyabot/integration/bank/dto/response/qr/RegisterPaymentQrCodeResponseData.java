package ru.snptech.businessbanyabot.integration.bank.dto.response.qr;

import ru.snptech.businessbanyabot.integration.bank.dto.common.QrImage;

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
