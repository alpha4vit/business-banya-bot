package ru.snptech.ritualbitrixbot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.repository.TelegramUserRepository;
import ru.snptech.ritualbitrixbot.telegram.scenario.AbstractScenario;

import java.util.Map;

import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;

@Component
@RequiredArgsConstructor
public class UserManageAssistantScenario extends AbstractScenario {
    private final TelegramClient telegramClient;
    private final TelegramUserRepository telegramUserRepository;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        var text = tgUpdate.getMessage().getText();
        if (text.startsWith("/addAssistant ")) {
            var username = text.substring("/addAssistant ".length());
            var user = telegramUserRepository.findByTelegramUsername(username);
            if (user == null) {
                telegramClient.execute(
                        SendMessage.builder()
                                .chatId(CHAT_ID.getValue(requestContext))
                                .text("Пользователь не зашел в бота!")
                                .build()
                );
            } else {
                telegramClient.execute(
                        SendMessage.builder()
                                .chatId(CHAT_ID.getValue(requestContext))
                                .text("Пользователю выдан доступ к боту!")
                                .build()
                );
                telegramClient.execute(
                        SendMessage.builder()
                                .chatId(user.getChatId())
                                .text("Вы подключены к партнеру!")
                                .build()
                );
                user.setPartnerAccount(AUTHENTICATED_USER.getValue(requestContext));
                user.setRegion(null);
                telegramUserRepository.save(user);
            }
        } else if (text.startsWith("/removeAssistant ")) {
            var username = text.substring("/removeAssistant ".length());
            var user = telegramUserRepository.findByTelegramUsername(username);
            if (user == null) {
                telegramClient.execute(
                        SendMessage.builder()
                                .chatId(CHAT_ID.getValue(requestContext))
                                .text("Пользователь не найден!")
                                .build()
                );
            } else {
                telegramClient.execute(
                        SendMessage.builder()
                                .chatId(CHAT_ID.getValue(requestContext))
                                .text("Пользователю отключен от бота!")
                                .build()
                );
                telegramClient.execute(
                        SendMessage.builder()
                                .chatId(user.getChatId())
                                .text("Вы были отключены от бота!")
                                .build()
                );
                user.setPartnerAccount(null);
                telegramUserRepository.save(user);
            }
        }
    }

    public static boolean isNeedInvoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (!tgUpdate.hasMessage() || !tgUpdate.getMessage().hasText()) {
            return false;
        }
        var text = tgUpdate.getMessage().getText();
        return text.startsWith("/addAssistant ") || text.startsWith("/removeAssistant ");
    }

}
