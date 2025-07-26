package ru.snptech.businessbanyabot.integrations.bank.dto.response;

public record QrImage(
    Integer width,
    Integer length,
    String mediaType,
    String content
) {
}
