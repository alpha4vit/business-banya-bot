package ru.snptech.businessbanyabot.telegram.client;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
public class TelegramClientAdapter {

    private final TelegramClient telegramClient;

    @SneakyThrows
    public void sendMessage(Long chatId, String text) {
        var message = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .parseMode(ParseMode.MARKDOWNV2)
            .build();

        telegramClient.execute(message);
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String text, String parseMode) {
        var message = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .parseMode(parseMode)
            .build();

        telegramClient.execute(message);
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        var message = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .replyMarkup(replyKeyboard)
            .parseMode(ParseMode.MARKDOWNV2)
            .build();

        telegramClient.execute(message);
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard, String parseMode) {
        var message = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .replyMarkup(replyKeyboard)
            .parseMode(parseMode)
            .build();

        telegramClient.execute(message);
    }

    @SneakyThrows
    public void releaseCallback(String callbackId) {
        telegramClient.execute(new AnswerCallbackQuery(callbackId));
    }

}
