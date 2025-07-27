package ru.snptech.businessbanyabot.integrations.bank.dto.request.qr;

public record QRCodeRequestParams(
    String width,
    String height,
    String mediaType
) {
}
