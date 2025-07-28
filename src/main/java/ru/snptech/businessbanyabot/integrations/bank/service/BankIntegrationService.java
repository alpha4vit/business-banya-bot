package ru.snptech.businessbanyabot.integrations.bank.service;

import ru.snptech.businessbanyabot.integrations.bank.dto.common.QrType;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.qr.RegisterPaymentQrCodeRequest;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.webhook.CreateWebhookRequest;
import ru.snptech.businessbanyabot.integrations.bank.dto.response.qr.RegisterPaymentQrCodeResponse;

public interface BankIntegrationService {

    RegisterPaymentQrCodeResponse registerQrCode(RegisterPaymentQrCodeRequest request);

    String createWebhook(CreateWebhookRequest request);
}
