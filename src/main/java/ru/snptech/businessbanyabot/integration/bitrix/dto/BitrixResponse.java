package ru.snptech.businessbanyabot.integration.bitrix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BitrixResponse<T>(
    T result
) {
}
