package ru.snptech.businessbanyabot.integrations.bank.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.integrations.bank.properties.BankIntegrationProperties;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

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
