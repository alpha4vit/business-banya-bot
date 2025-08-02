package ru.snptech.businessbanyabot.integration.bitrix.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FileDto(
    @JsonProperty("id")
    int id,

    @JsonProperty("showUrl")
    String showUrl,

    @JsonProperty("downloadUrl")
    String downloadUrl
) {
}