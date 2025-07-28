package ru.snptech.businessbanyabot.integrations.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.integrations.bank.dto.common.QrImage;
import ru.snptech.businessbanyabot.integrations.bank.dto.common.QrType;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.qr.RegisterPaymentQrCodeData;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.qr.RegisterPaymentQrCodeRequest;
import ru.snptech.businessbanyabot.integrations.bank.properties.BankIntegrationProperties;

@Component
@RequiredArgsConstructor
public class RegisterQrCodeRequestBuilder {

    private static final String PAYMENT_PURPOSE = "Оплата счета из телеграмм-бота";
    private static final String SOURCE_NAME = "Телеграмм бот";

    private final BankIntegrationProperties bankIntegrationProperties;

    public RegisterPaymentQrCodeRequest build(QrType type) {
        var qrCodeSettings = bankIntegrationProperties.getQrCodeSettings();
        var imageSettings = qrCodeSettings.getImageSettings();

        return new RegisterPaymentQrCodeRequest(
            new RegisterPaymentQrCodeData(
                qrCodeSettings.getMoneyAmount(),
                qrCodeSettings.getCurrency(),
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
