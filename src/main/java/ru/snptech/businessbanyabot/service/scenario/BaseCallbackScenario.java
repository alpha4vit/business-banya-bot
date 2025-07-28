package ru.snptech.businessbanyabot.service.scenario;

import lombok.SneakyThrows;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.TG_UPDATE;

public abstract class BaseCallbackScenario extends AbstractScenario {

    public BaseCallbackScenario(TelegramClientAdapter telegramClientAdapter) {
        super(telegramClientAdapter);
    }

    @SneakyThrows
    protected void releaseCallback(Map<String, Object> requestContext) {
        var callbackId = TG_UPDATE.getValue(requestContext).getCallbackQuery().getId();

        telegramClientAdapter.releaseCallback(callbackId);
    }

    protected static String extractCallbackPrefix(String callback) {
        return callback.split("_")[0] + "_";
    }

    protected static String extractCallbackPostfix(String callback) {
        return callback.split("_")[1];
    }

}
