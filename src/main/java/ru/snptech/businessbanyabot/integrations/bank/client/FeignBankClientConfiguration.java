package ru.snptech.businessbanyabot.integrations.bank.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.snptech.businessbanyabot.integrations.bank.properties.BankIntegrationProperties;

@Configuration
public class FeignBankClientConfiguration {

    @Bean
    public FeignBankClientAuthorizationInterceptor authRequestInterceptor(
        BankIntegrationProperties bankIntegrationProperties
    ) {
        return new FeignBankClientAuthorizationInterceptor(bankIntegrationProperties);
    }

}
