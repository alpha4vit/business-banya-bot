package ru.snptech.businessbanyabot.integration.bitrix.client;

import feign.Logger;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.FormHttpMessageConverter;

public class FeignBitrixClientConfiguration {

//    @Bean
//    Logger.Level feignLoggerLevel() {
//        return Logger.Level.FULL;
//    }

    @Bean
    public Encoder feignFormEncoder() {
        return new FormEncoder(new SpringEncoder(() -> new HttpMessageConverters(new FormHttpMessageConverter())));
    }

}
