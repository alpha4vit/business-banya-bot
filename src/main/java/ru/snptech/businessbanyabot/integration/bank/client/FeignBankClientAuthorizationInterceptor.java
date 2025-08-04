package ru.snptech.businessbanyabot.integration.bank.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.snptech.businessbanyabot.integration.bank.properties.BankIntegrationProperties;

@Slf4j
@RequiredArgsConstructor
public class FeignBankClientAuthorizationInterceptor implements RequestInterceptor {

    private final BankIntegrationProperties bankIntegrationProperties;

    @Override
    @SneakyThrows
    public void apply(final RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", "Bearer %s".formatted(bankIntegrationProperties.getToken()));
    }
}
