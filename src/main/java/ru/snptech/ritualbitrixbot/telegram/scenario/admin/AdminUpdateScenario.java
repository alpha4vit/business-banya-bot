package ru.snptech.ritualbitrixbot.telegram.scenario.admin;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.entity.Role;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;
import ru.snptech.ritualbitrixbot.repository.TelegramUserRepository;
import ru.snptech.ritualbitrixbot.telegram.MenuConstants;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;
import ru.snptech.ritualbitrixbot.telegram.scenario.AbstractScenario;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;

@Component
@RequiredArgsConstructor
public class AdminUpdateScenario extends AbstractScenario {
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramClient telegramClient;

    public static final String DELETE_PARTNER_FROM_REGION_COMMAND = "Удалить партнера";
    public static final String DELETE_PARTNER_PREFIX = "DP_";

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.hasCallbackQuery()) {
            var callbackData = tgUpdate.getCallbackQuery().getData();
            if (callbackData.startsWith("AA_")) {
                var moderationUserChatId = callbackData.substring(3);
                var moderationUser = telegramUserRepository.findByChatId(Long.parseLong(moderationUserChatId));
                moderationUser.setActive(true);
                telegramUserRepository.save(moderationUser);
                deleteMessageById(CHAT_ID.getValue(requestContext), tgUpdate.getCallbackQuery().getMessage().getMessageId());
                notifyUserAboutSuccessfulModeration(moderationUser);
            } else if (callbackData.startsWith("AR_")) {
                var moderationUserChatId = callbackData.substring(3);
                var moderationUser = telegramUserRepository.findByChatId(Long.parseLong(moderationUserChatId));
                moderationUser.setActive(false);
                telegramUserRepository.save(moderationUser);
                deleteMessageById(CHAT_ID.getValue(requestContext), tgUpdate.getCallbackQuery().getMessage().getMessageId());
                notifyUserAboutSuccessfulModeration(moderationUser);
            } else if (callbackData.startsWith(DELETE_PARTNER_PREFIX)) {
                var regionId = callbackData.substring(3);
                var userToBlock = telegramUserRepository.findAllByRegion_Id(Long.parseLong(regionId));
                userToBlock.getFirst().setActive(false);
                telegramUserRepository.save(userToBlock.getFirst());
                var sendMessage = SendMessage.builder()
                        .chatId(CHAT_ID.getValue(requestContext))
                        .text("Партнер отключен от системы!")
                        .build();
                telegramClient.execute(sendMessage);
            }
        } else if (tgUpdate.hasMessage()) {
            switch (tgUpdate.getMessage().getText()) {
                case DELETE_PARTNER_FROM_REGION_COMMAND -> {
                    var regionsWithPartners = telegramUserRepository.findAllByRoleAndActiveTrue(Role.USER)
                            .stream()
                            .map(TelegramUser::getRegion)
                            .filter(Objects::nonNull)
                            .distinct().toList();
                    if (regionsWithPartners.isEmpty()) {
                        var sendMessage = SendMessage.builder()
                                .chatId(CHAT_ID.getValue(requestContext))
                                .text("В данный момент партнеров")
                                .build();
                        telegramClient.execute(sendMessage);
                    } else {
                        var keyboardRows = regionsWithPartners.stream().map(region -> new InlineKeyboardRow(
                                InlineKeyboardButton.builder()
                                        .text(region.getName())
                                        .callbackData(DELETE_PARTNER_PREFIX + region.getId())
                                        .build()
                        )).toList();
                        var sendMessage = SendMessage.builder()
                                .chatId(CHAT_ID.getValue(requestContext))
                                .text("Выберите партнера для удаления")
                                .replyMarkup(new InlineKeyboardMarkup(keyboardRows))
                                .build();
                        telegramClient.execute(sendMessage);
                    }
                }
                default -> {
                    var sendMessage = SendMessage.builder()
                            .chatId(CHAT_ID.getValue(requestContext))
                            .text("Меню администратора")
                            .replyMarkup(MenuConstants.adminMainMenu())
                            .build();
                    telegramClient.execute(sendMessage);
                }
            }
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
                        .replyMarkup(MenuConstants.createUserMainMenu(
                                telegramUser.getPartnerAccount() == null
                        ))
                        .build()
        );
    }
}
