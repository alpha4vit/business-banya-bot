package ru.snptech.businessbanyabot.telegram.client;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Slf4j
public class ExceptionSafeTelegramClient extends OkHttpTelegramClient {
    public ExceptionSafeTelegramClient(String botToken) {
        super(botToken);
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) {
        try {
            return super.execute(method);
        } catch (TelegramApiException e) {
            log.error("Error while executing method: {}", method, e);
            return null;
        }
    }
}
