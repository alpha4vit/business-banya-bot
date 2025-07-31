package ru.snptech.businessbanyabot.integration.bank.dto.request.qr;

import ru.snptech.businessbanyabot.integration.bank.dto.common.QrImage;

public record RegisterPaymentQrCodeData(
    Integer amount,
    String currency,
    String paymentPurpose,
    String qrcType,
    QrImage imageParams,
    String sourceName,
    Long ttl
) {
}
