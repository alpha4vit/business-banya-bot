package ru.snptech.businessbanyabot.integration.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.integration.bank.dto.common.QrImage;
import ru.snptech.businessbanyabot.integration.bank.dto.common.QrType;
import ru.snptech.businessbanyabot.integration.bank.dto.request.qr.RegisterPaymentQrCodeData;
import ru.snptech.businessbanyabot.integration.bank.dto.request.qr.RegisterPaymentQrCodeRequest;
import ru.snptech.businessbanyabot.integration.bank.properties.BankIntegrationProperties;
import ru.snptech.businessbanyabot.propterties.ApplicationProperties;

@Component
@RequiredArgsConstructor
public class RegisterQrCodeRequestBuilder {

    private static final String PAYMENT_PURPOSE = "Оплата счета из телеграмм-бота";
    private static final String SOURCE_NAME = "Телеграмм бот";

    private final ApplicationProperties applicationProperties;
    private final BankIntegrationProperties bankIntegrationProperties;

    public RegisterPaymentQrCodeRequest build(QrType type, Integer amount) {
        var qrCodeSettings = bankIntegrationProperties.getQrCodeSettings();
        var imageSettings = qrCodeSettings.getImageSettings();
        var moneyAmount = amount != null ? amount : applicationProperties.getPayment().getAmount();

        return new RegisterPaymentQrCodeRequest(
            new RegisterPaymentQrCodeData(
                moneyAmount,
                applicationProperties.getPayment().getCurrency(),
                PAYMENT_PURPOSE,
                type.getValue(),
                new QrImage(
                    imageSettings.getSize(),
                    imageSettings.getSize(),
                    imageSettings.getMediaType()
                ),
                SOURCE_NAME,
                qrCodeSettings.getTtl().toMinutes()
            )
        );
    }

}
