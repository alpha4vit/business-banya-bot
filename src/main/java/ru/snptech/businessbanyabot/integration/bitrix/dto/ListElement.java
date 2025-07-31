package ru.snptech.businessbanyabot.integration.bitrix.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ListElement(
    @JsonProperty("ID")
    String id,
    @JsonProperty("NAME")
    String name
) {
}
