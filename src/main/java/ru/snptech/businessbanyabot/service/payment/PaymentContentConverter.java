package ru.snptech.businessbanyabot.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.snptech.businessbanyabot.model.payment.PaymentContent;

import java.io.IOException;

@Converter
public class PaymentContentConverter implements AttributeConverter<PaymentContent, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(PaymentContent attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Ошибка сериализации PaymentContent", e);
        }
    }

    @Override
    public PaymentContent convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return objectMapper.readValue(dbData, PaymentContent.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка десериализации PaymentContent", e);
        }
    }
}
