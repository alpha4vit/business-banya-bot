package ru.snptech.businessbanyabot.telegram.scenario;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.snptech.businessbanyabot.telegram.MessageConstants;

import java.util.Map;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;

abstract public class AbstractScenario {

    protected static String getMessageText(Map<String, Object> requestContext) {
        return TG_UPDATE.getValue(requestContext).getMessage().getText();
    }

    protected static SendMessage createSendMessage(String chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
    }

    protected static SendMessage createSendMessage(String chatId, String text, String parseMode) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(parseMode)
                .build();
    }

    protected static SendMessage createSendMessage(String chatId, String text, ReplyKeyboard replyKeyboard) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard)
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
    }

    protected static SendMessage createSendMessage(String chatId, String text, ReplyKeyboard replyKeyboard, String parseMode) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard)
                .parseMode(parseMode)
                .build();
    }

    protected static SendMessage sentToBitrix(String chatId) {
        return createSendMessage(chatId, MessageConstants.DATA_SENT_TO_BITRIX_MESSAGE, (String) null);
    }

    protected static void escapeStartCommandFromUtm(Message tgMessage) {
        if (tgMessage.getText().startsWith("/start ")) tgMessage.setText("/start");
    }

    protected static String extractDealId(String input) {
        return input.split("-")[1];
    }

}
