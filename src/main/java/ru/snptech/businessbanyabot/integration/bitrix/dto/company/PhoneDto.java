package ru.snptech.businessbanyabot.integration.bitrix.dto.company;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PhoneDto(
    @JsonProperty("ID")
    String phoneId,

    @JsonProperty("VALUE_TYPE")
    String valueType,

    @JsonProperty("VALUE")
    String value,

    @JsonProperty("TYPE_ID")
    String typeId
) {
}