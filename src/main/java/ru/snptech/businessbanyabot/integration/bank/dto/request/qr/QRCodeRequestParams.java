package ru.snptech.businessbanyabot.integration.bank.dto.request.qr;

public record QRCodeRequestParams(
    String width,
    String height,
    String mediaType
) {
}
