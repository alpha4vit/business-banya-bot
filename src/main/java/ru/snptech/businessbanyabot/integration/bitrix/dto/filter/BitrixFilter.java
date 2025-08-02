package ru.snptech.businessbanyabot.integration.bitrix.dto.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BitrixFilter(
    @JsonProperty("entity_type")
    String entityType,

    String type,

    List<String> values
) {
}
