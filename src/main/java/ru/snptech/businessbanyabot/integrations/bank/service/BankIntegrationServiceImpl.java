package ru.snptech.businessbanyabot.integrations.bank.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.integrations.bank.client.FeignBankClient;
import ru.snptech.businessbanyabot.integrations.bank.dto.common.QrType;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.webhook.CreateWebhookRequest;
import ru.snptech.businessbanyabot.integrations.bank.dto.response.qr.RegisterPaymentQrCodeResponse;
import ru.snptech.businessbanyabot.integrations.bank.properties.BankIntegrationProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankIntegrationServiceImpl implements BankIntegrationService {

    private final RegisterQrCodeRequestBuilder registerQrCodeRequestBuilder;
    private final BankIntegrationProperties bankIntegrationProperties;
    private final FeignBankClient feignBankClient;

    @Override
    @SneakyThrows
    public RegisterPaymentQrCodeResponse registerQrCode(QrType type) {
        var request = registerQrCodeRequestBuilder.build(type);

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
}
