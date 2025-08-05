package ru.snptech.businessbanyabot.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignOkHttpConfiguration {

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

}
