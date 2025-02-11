package ru.snptech.ritualbitrixbot.telegram.scenario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.snptech.ritualbitrixbot.entity.Role;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;
import ru.snptech.ritualbitrixbot.repository.TelegramUserRepository;
import ru.snptech.ritualbitrixbot.telegram.TelegramUtils;

import java.util.Map;

import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.IS_ADMIN;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CHAT_ID;

@Component
@RequiredArgsConstructor
public class AuthenticationScenario {
    private final TelegramUserRepository telegramUserRepository;

    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        var chatId = TelegramUtils.extractChatIdFromUpdate(tgUpdate);
        var user = telegramUserRepository.findByChatId(chatId);
        if (user == null) {
            var tgUser = TelegramUtils.extractUserFromUpdate(tgUpdate);
            user = new TelegramUser();
            user.setChatId(chatId);
            user.setTelegramFirstName(tgUser.getFirstName());
            user.setTelegramLastName(tgUser.getLastName());
            user.setTelegramUsername(tgUser.getUserName());
            user.setRole(Role.USER);
            user = telegramUserRepository.save(user);
        }
        AUTHENTICATED_USER.setValue(requestContext, user);
        IS_ADMIN.setValue(requestContext, user.getRole() == Role.ADMIN);
        CHAT_ID.setValue(requestContext, user.getChatId().toString());
    }

    private static String extractUtm(Update update) {
        var message = update.getMessage().getText();
        if (message.contains(" ")) {
            return message.split(" ")[1];
        } else {
            return null;
        }
    }

}
