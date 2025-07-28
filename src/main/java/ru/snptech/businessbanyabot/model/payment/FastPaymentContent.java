package ru.snptech.businessbanyabot.model.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FastPaymentContent extends PaymentContent {
    private PaymentType type;
    private String base64Image;
    private String link;
}