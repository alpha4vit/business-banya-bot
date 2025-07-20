package ru.snptech.businessbanyabot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.telegram.client.ExceptionSafeTelegramClient;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

@Getter
@Configuration
public class TelegramConfiguration {
    @Value("${application.telegram.token}")
    private String token;

    @Bean
    @Primary
    public TelegramClient telegramClient() {
        return new ExceptionSafeTelegramClient(token);
    }

    @Bean
    public TelegramClientAdapter telegramClientAdapter(TelegramClient telegramClient) {
        return new TelegramClientAdapter(telegramClient);
    }

}
