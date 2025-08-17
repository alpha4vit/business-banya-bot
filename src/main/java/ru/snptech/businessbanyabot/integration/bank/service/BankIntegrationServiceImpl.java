package ru.snptech.businessbanyabot.integration.bank.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.integration.bank.client.FeignBankClient;
import ru.snptech.businessbanyabot.integration.bank.dto.request.qr.RegisterPaymentQrCodeRequest;
import ru.snptech.businessbanyabot.integration.bank.dto.request.webhook.CreateWebhookRequest;
import ru.snptech.businessbanyabot.integration.bank.dto.response.qr.RegisterPaymentQrCodeResponse;
import ru.snptech.businessbanyabot.integration.bank.properties.BankIntegrationProperties;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankIntegrationServiceImpl implements BankIntegrationService {


    private final BankIntegrationProperties bankIntegrationProperties;
    private final FeignBankClient feignBankClient;

    @Override
    @SneakyThrows
    public RegisterPaymentQrCodeResponse registerQrCode(RegisterPaymentQrCodeRequest request) {
        return feignBankClient.registerQrCode(
                bankIntegrationProperties.getApiVersion(),
                bankIntegrationProperties.getMerchantId(),
                bankIntegrationProperties.getAccountId(),
                request
            )
            .getBody();
    }

    @Override
    @SneakyThrows
    public String createWebhook(final CreateWebhookRequest request) {
        return feignBankClient.createWebhook(
                bankIntegrationProperties.getApiVersion(),
                bankIntegrationProperties.getClientId(),
                request
            )
            .getBody();
    }

    @SneakyThrows
    public String getWebhooks() {
        return feignBankClient.getWebhooks(
                bankIntegrationProperties.getApiVersion(),
                bankIntegrationProperties.getClientId()
            )
            .getBody();
    }
}
