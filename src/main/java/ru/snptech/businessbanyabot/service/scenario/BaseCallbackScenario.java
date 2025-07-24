package ru.snptech.businessbanyabot.service.scenario;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.TG_UPDATE;

@RequiredArgsConstructor
public abstract class BaseCallbackScenario extends AbstractScenario {

    protected final TelegramClientAdapter telegramClientAdapter;

    @SneakyThrows
    protected void releaseCallback(Map<String, Object> requestContext) {
        var callbackId = TG_UPDATE.getValue(requestContext).getCallbackQuery().getId();
        telegramClientAdapter.releaseCallback(callbackId);
    }

    @SneakyThrows
    protected void sendMessage(Map<String, Object> requestContext, String message) {
        telegramClientAdapter.sendMessage(
            CHAT_ID.getValue(requestContext, Long.class),
            message
        );
    }

    @SneakyThrows
    protected void sendMessage(Map<String, Object> requestContext, String message, ReplyKeyboard replyKeyboard) {
        telegramClientAdapter.sendMessage(
            CHAT_ID.getValue(requestContext, Long.class),
            message,
            replyKeyboard
        );
    }

    protected static String extractCallbackPrefix(String callback) {
        return callback.split("_")[0] + "_";
    }

    protected static String extractCallbackPostfix(String callback) {
        return callback.split("_")[1];
    }

}
