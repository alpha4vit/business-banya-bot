package ru.snptech.businessbanyabot.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import ru.snptech.businessbanyabot.config.TelegramConfiguration;

@Component
@RequiredArgsConstructor
public class TelegramBot implements SpringLongPollingBot {
    private final TelegramConfiguration telegramConfiguration;
    private final UpdateConsumer updateConsumer;

    @Override
    public String getBotToken() {
        return telegramConfiguration.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
