package ru.snptech.businessbanyabot.integration.bitrix.dto.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;

@Embeddable
public record PhoneDto(
    @EmbeddedId
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