package ru.snptech.businessbanyabot.integrations.bank.dto.response.qr;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.snptech.businessbanyabot.integrations.bank.dto.common.MetaData;

public record RegisterPaymentQrCodeResponse(
    @JsonProperty(value = "Data")
    RegisterPaymentQrCodeResponseData data,

    @JsonProperty(value = "Links")
    RegisterPaymentQrCodeLinks links,

    @JsonProperty(value = "Meta")
    MetaData meta
) {
}
