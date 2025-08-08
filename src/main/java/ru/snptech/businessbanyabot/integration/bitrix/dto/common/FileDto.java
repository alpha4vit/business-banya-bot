package ru.snptech.businessbanyabot.integration.bitrix.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Embeddable
public record FileDto(
    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    Integer fileId,

    @JsonProperty("showUrl")
    String showUrl,

    @JsonProperty("downloadUrl")
    String downloadUrl
) {
}