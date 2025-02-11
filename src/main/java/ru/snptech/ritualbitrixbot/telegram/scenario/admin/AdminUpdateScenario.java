package ru.snptech.ritualbitrixbot.telegram.scenario.admin;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;
import ru.snptech.ritualbitrixbot.repository.TelegramUserRepository;
import ru.snptech.ritualbitrixbot.telegram.MenuConstants;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;
import ru.snptech.ritualbitrixbot.telegram.scenario.AbstractScenario;

import java.util.Map;

import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;

@Component
@RequiredArgsConstructor
public class AdminUpdateScenario extends AbstractScenario {
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramClient telegramClient;

    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.hasCallbackQuery()) {
            var callbackData = tgUpdate.getCallbackQuery().getData();
            var moderationUserChatId = callbackData.substring(3);
            var moderationUser = telegramUserRepository.findByChatId(Long.parseLong(moderationUserChatId));
            if (callbackData.startsWith("AA_")) {
                moderationUser.setActive(true);
            } else if (callbackData.startsWith("AR_")) {
                moderationUser.setActive(false);
            }
            deleteMessageById(CHAT_ID.getValue(requestContext), tgUpdate.getCallbackQuery().getMessage().getMessageId());
            notifyUserAboutSuccessfulModeration(moderationUser);
        }
    }

    @SneakyThrows
    private void deleteMessageById(String chatId, Integer messageId) {
        telegramClient.execute(DeleteMessage.builder().chatId(chatId).messageId(messageId).build());
    }

    @SneakyThrows
    private void notifyUserAboutSuccessfulModeration(TelegramUser telegramUser) {
        telegramClient.execute(
                SendMessage.builder()
                        .chatId(telegramUser.getChatId())
                        .text(MessageConstants.MODERATION_APPROVED_TO_USER_MESSAGE)
                        .replyMarkup(MenuConstants.createUserMainMenu())
                        .build()
        );
    }
}
