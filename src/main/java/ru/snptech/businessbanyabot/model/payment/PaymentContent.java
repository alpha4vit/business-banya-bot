package ru.snptech.businessbanyabot.model.payment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = FastPaymentContent.class, name = "FAST_PAYMENT"),
    @JsonSubTypes.Type(value = InvoiceContent.class, name = "INVOICE")
})
public abstract class PaymentContent {

    public abstract String getExternalPayload();
}