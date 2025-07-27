package ru.snptech.businessbanyabot.integrations.bank.dto.request.qr;

import ru.snptech.businessbanyabot.integrations.bank.dto.common.QrImage;

public record RegisterPaymentQrCodeData(
    Integer amount,
    String currency,
    String paymentPurpose,
    String qrcType,
    QrImage imageParams,
    String sourceName,
    Integer ttl
) {
}
