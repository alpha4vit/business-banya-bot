package ru.snptech.businessbanyabot.integration.bank.dto.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorMessage(

) {
}
