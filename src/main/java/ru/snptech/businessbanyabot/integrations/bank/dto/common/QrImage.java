package ru.snptech.businessbanyabot.integrations.bank.dto.common;

public record QrImage(
    Integer width,
    Integer height,
    String mediaType,
    String content
) {

    public QrImage(Integer width, Integer height, String mediaType) {
        this(
            width,
            height,
            mediaType,
            null
        );
    }
}
