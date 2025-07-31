package ru.snptech.businessbanyabot.integration.bitrix.client;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class FeignBitrixClientConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
