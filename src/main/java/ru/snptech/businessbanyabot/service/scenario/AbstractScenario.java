package ru.snptech.businessbanyabot.service.scenario;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.io.File;
import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.CHAT_ID;

@RequiredArgsConstructor
abstract public class AbstractScenario {

    protected final TelegramClientAdapter telegramClientAdapter;

    @SneakyThrows
    protected Integer sendMessage(Map<String, Object> requestContext, String message) {
        return telegramClientAdapter.sendMessage(
            CHAT_ID.getValue(requestContext, Long.class),
            message
        );
    }

    @SneakyThrows
    protected Integer sendMessage(Map<String, Object> requestContext, String message, ReplyKeyboard replyKeyboard) {
        return telegramClientAdapter.sendMessage(
            CHAT_ID.getValue(requestContext, Long.class),
            message,
            replyKeyboard
        );
    }

    @SneakyThrows
    protected void sendPhoto(Map<String, Object> requestContext, File file) {
        telegramClientAdapter.sendPhoto(
            CHAT_ID.getValue(requestContext, Long.class),
            file
        );
    }

    @SneakyThrows
    protected void sendPhoto(Map<String, Object> requestContext, File file, String caption) {
        telegramClientAdapter.sendPhoto(
            CHAT_ID.getValue(requestContext, Long.class),
            file,
            caption
        );
    }

    @SneakyThrows
    protected void sendPhoto(
        Map<String, Object> requestContext,
        File file,
        String caption,
        InlineKeyboardMarkup replyKeyboard
    ) {
        telegramClientAdapter.sendPhoto(
            CHAT_ID.getValue(requestContext, Long.class),
            file,
            caption,
            replyKeyboard
        );
    }

    @SneakyThrows
    protected void sendFile(Map<String, Object> requestContext, File file) {
        telegramClientAdapter.sendFile(
            CHAT_ID.getValue(requestContext, Long.class),
            file
        );
    }

    @SneakyThrows
    protected Integer updateMessage(Integer messageId, Map<String, Object> requestContext, String message, InlineKeyboardMarkup replyKeyboard) {
        return telegramClientAdapter.updateMessage(
            messageId,
            CHAT_ID.getValue(requestContext, Long.class),
            message,
            replyKeyboard
        );
    }
}
