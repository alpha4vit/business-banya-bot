package ru.snptech.businessbanyabot.integrations.bank.dto.request;

public record QRCodeRequestParams(
    String width,
    String height,
    String mediaType
) {
}
