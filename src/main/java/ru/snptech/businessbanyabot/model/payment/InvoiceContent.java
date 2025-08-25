package ru.snptech.businessbanyabot.model.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceContent extends PaymentContent {

    @Override
    public String getExternalPayload() {
        return "";
    }
}