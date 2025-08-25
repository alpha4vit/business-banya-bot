package ru.snptech.businessbanyabot.integration.bank.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
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

    // TODO add call method and handle all incoming errors from bank in human readable view
    @Override
    @SneakyThrows
    public RegisterPaymentQrCodeResponse registerQrCode(RegisterPaymentQrCodeRequest request) {
        try {
            return feignBankClient.registerQrCode(
                    bankIntegrationProperties.getApiVersion(),
                    bankIntegrationProperties.getMerchantId(),
                    bankIntegrationProperties.getAccountId(),
                    request
                )
                .getBody();
        } catch (FeignException.FeignClientException e) {
            throw new BusinessBanyaInternalException.INTERNAL_ERROR(e.getMessage());
        }
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
