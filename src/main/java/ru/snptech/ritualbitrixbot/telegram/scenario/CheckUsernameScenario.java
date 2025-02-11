package ru.snptech.ritualbitrixbot.telegram.scenario;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;

import java.util.Map;

import static ru.snptech.ritualbitrixbot.telegram.TelegramUtils.extractChatIdFromUpdate;
import static ru.snptech.ritualbitrixbot.telegram.TelegramUtils.extractUserFromUpdate;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.HAS_USERNAME;

@Component
@RequiredArgsConstructor
public class CheckUsernameScenario extends AbstractScenario {
    private final TelegramClient telegramClient;
    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var username = extractUserFromUpdate(TG_UPDATE.getValue(requestContext)).getUserName();
        var chatId = extractChatIdFromUpdate(TG_UPDATE.getValue(requestContext));
        if (username == null || username.isEmpty()) {
            telegramClient.execute(createSendMessage(String.valueOf(chatId), MessageConstants.NO_USERNAME_MESSAGE));
            HAS_USERNAME.setValue(requestContext, Boolean.FALSE);
        } else {
            HAS_USERNAME.setValue(requestContext, Boolean.TRUE);
        }
    }
}
