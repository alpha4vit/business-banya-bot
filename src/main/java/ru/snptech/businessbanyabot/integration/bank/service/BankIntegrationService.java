package ru.snptech.businessbanyabot.integration.bank.service;

import ru.snptech.businessbanyabot.integration.bank.dto.request.qr.RegisterPaymentQrCodeRequest;
import ru.snptech.businessbanyabot.integration.bank.dto.request.webhook.CreateWebhookRequest;
import ru.snptech.businessbanyabot.integration.bank.dto.response.qr.RegisterPaymentQrCodeResponse;

import java.time.Instant;

public interface BankIntegrationService {

    RegisterPaymentQrCodeResponse registerQrCode(RegisterPaymentQrCodeRequest request);

    String createWebhook(CreateWebhookRequest request);

    String getWebhooks();
}
