package ru.snptech.businessbanyabot.integration.bank.client;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.snptech.businessbanyabot.integration.bank.properties.BankIntegrationProperties;

public class FeignBankClientConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public FeignBankClientAuthorizationInterceptor authRequestInterceptor(
        BankIntegrationProperties bankIntegrationProperties
    ) {
        return new FeignBankClientAuthorizationInterceptor(bankIntegrationProperties);
    }

}
