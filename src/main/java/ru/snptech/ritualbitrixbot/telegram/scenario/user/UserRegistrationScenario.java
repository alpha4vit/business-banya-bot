package ru.snptech.ritualbitrixbot.telegram.scenario.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.entity.Role;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;
import ru.snptech.ritualbitrixbot.repository.RegionRepository;
import ru.snptech.ritualbitrixbot.repository.TelegramUserRepository;
import ru.snptech.ritualbitrixbot.service.UserContextService;
import ru.snptech.ritualbitrixbot.telegram.MenuConstants;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;
import ru.snptech.ritualbitrixbot.telegram.scenario.AbstractScenario;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.snptech.ritualbitrixbot.telegram.TelegramUtils.escapeMarkdownV2;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.SCENARIO_STEP;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;

@Component
@RequiredArgsConstructor
public class UserRegistrationScenario extends AbstractScenario {
    private final RegionRepository regionRepository;
    private final UserContextService userContextService;
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramClient telegramClient;

    public void invoke(Map<String, Object> requestContext) {
        var scenarioStep = getUserStep(requestContext);
        switch (scenarioStep) {
            case WAITING_FULLNAME -> processScenarioFullnameInput(requestContext);
            case WAITING_REGION -> processScenarioRegionSelect(requestContext);
            case WAITING_CONFIRMATION -> processScenarioConfirmationSelect(requestContext);
            default -> processScenarioInit(requestContext);
        }
    }

    private void processScenarioInit(Map<String, Object> requestContext) {
        sendConstantMessage(requestContext, MessageConstants.REGISTRATION_FULLNAME_INPUT_MESSAGE);
        userContextService.updateUserContext(AUTHENTICATED_USER.getValue(requestContext), SCENARIO_STEP, Steps.WAITING_FULLNAME.name());
    }

    private void processScenarioFullnameInput(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.hasMessage() && tgUpdate.getMessage().hasText()) {
            var authUser = AUTHENTICATED_USER.getValue(requestContext);
            authUser.setFullName(tgUpdate.getMessage().getText());
            authUser = telegramUserRepository.save(authUser);
            if (authUser.getRegion() != null) {
                userContextService.updateUserContext(authUser, SCENARIO_STEP, Steps.WAITING_CONFIRMATION.name());
                sendConstantMessage(
                        requestContext,
                        MessageConstants.REGISTRATION_CONFIRMATION_INPUT_MESSAGE.formatted(
                                escapeMarkdownV2(authUser.getFullName()),
                                escapeMarkdownV2(authUser.getRegion().getName())
                        ),
                        InlineKeyboardMarkup.builder().keyboard(List.of(
                                new InlineKeyboardRow(InlineKeyboardButton.builder().text("Подтвердить").callbackData(Callbacks.CONFIRM.name()).build()),
                                new InlineKeyboardRow(InlineKeyboardButton.builder().text("Изменить имя").callbackData(Callbacks.EDIT_FULLNAME.name()).build()),
                                new InlineKeyboardRow(InlineKeyboardButton.builder().text("Изменить регион").callbackData(Callbacks.EDIT_REGION.name()).build())
                        )).build()
                );
            } else {
                userContextService.updateUserContext(authUser, SCENARIO_STEP, Steps.WAITING_REGION.name());
                sendConstantMessage(
                        requestContext,
                        MessageConstants.REGISTRATION_REGION_INPUT_MESSAGE,
                        MenuConstants.createRegionSelectMenu(regionRepository.findAll())
                );
            }
        }
    }

    private void processScenarioRegionSelect(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.hasCallbackQuery() && StringUtils.isNumeric(tgUpdate.getCallbackQuery().getData())) {
            var region = regionRepository.findById(Long.parseLong(tgUpdate.getCallbackQuery().getData()));
            if (region.isEmpty()) return;
            var authUser = AUTHENTICATED_USER.getValue(requestContext);
            authUser.setRegion(region.get());
            authUser = telegramUserRepository.save(authUser);
            userContextService.updateUserContext(authUser, SCENARIO_STEP, Steps.WAITING_CONFIRMATION.name());
            sendConstantMessage(
                    requestContext,
                    MessageConstants.REGISTRATION_CONFIRMATION_INPUT_MESSAGE.formatted(
                            escapeMarkdownV2(authUser.getFullName()),
                            escapeMarkdownV2(authUser.getRegion().getName())
                    ),
                    InlineKeyboardMarkup.builder().keyboard(List.of(
                            new InlineKeyboardRow(InlineKeyboardButton.builder().text("Подтвердить").callbackData(Callbacks.CONFIRM.name()).build()),
                            new InlineKeyboardRow(InlineKeyboardButton.builder().text("Изменить имя").callbackData(Callbacks.EDIT_FULLNAME.name()).build()),
                            new InlineKeyboardRow(InlineKeyboardButton.builder().text("Изменить регион").callbackData(Callbacks.EDIT_REGION.name()).build())
                    )).build()
            );
        }
    }

    private void processScenarioConfirmationSelect(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.hasCallbackQuery() && Arrays.stream(Callbacks.values()).anyMatch(value -> value.name().equals(tgUpdate.getCallbackQuery().getData()))) {
            var callbackValue = Callbacks.valueOf(tgUpdate.getCallbackQuery().getData());
            var authUser = AUTHENTICATED_USER.getValue(requestContext);
            switch (callbackValue) {
                case CONFIRM -> {
                    userContextService.cleanUserContext(authUser);
                    authUser.setRegistered(true);
                    authUser = telegramUserRepository.save(authUser);
                    sendConstantMessage(requestContext, MessageConstants.REGISTRATION_SENT_TO_MODERATION_MESSAGE);
                    notifyAdminAboutModerationRequest(authUser);
                }
                case EDIT_REGION -> {
                    userContextService.updateUserContext(authUser, SCENARIO_STEP, Steps.WAITING_REGION.name());
                    sendConstantMessage(
                            requestContext,
                            MessageConstants.REGISTRATION_REGION_INPUT_MESSAGE,
                            MenuConstants.createRegionSelectMenu(regionRepository.findAll())
                    );
                }
                case EDIT_FULLNAME -> {
                    sendConstantMessage(requestContext, MessageConstants.REGISTRATION_FULLNAME_INPUT_MESSAGE);
                    userContextService.updateUserContext(AUTHENTICATED_USER.getValue(requestContext), SCENARIO_STEP, Steps.WAITING_FULLNAME.name());
                }
            }

        }
    }

    private void notifyAdminAboutModerationRequest(TelegramUser userForModeration) {
        var adminMenu = MenuConstants.createAdminModerationMenu(userForModeration);
        var text = String.format(
                MessageConstants.ADMIN_MODERATION_REQUEST_MESSAGE_TEMPLATE,
                escapeMarkdownV2(userForModeration.getFullName()),
                escapeMarkdownV2("@" + userForModeration.getTelegramUsername()),
                escapeMarkdownV2(userForModeration.getRegion().getName())
        );
        telegramUserRepository.findAllByRole(Role.ADMIN).forEach(admin -> {
            sendConstantMessage(
                    admin.getChatId().toString(),
                    text,
                    adminMenu
            );
        });
    }

    @SneakyThrows
    private void sendConstantMessage(Map<String, Object> requestContext, String text) {
        SendMessage message = createSendMessage(
                CHAT_ID.getValue(requestContext),
                text
        );
        telegramClient.execute(message);
    }

    @SneakyThrows
    private void sendConstantMessage(Map<String, Object> requestContext, String text, ReplyKeyboard replyKeyboard) {
        SendMessage message = createSendMessage(
                CHAT_ID.getValue(requestContext),
                text,
                replyKeyboard
        );
        telegramClient.execute(message);
    }

    @SneakyThrows
    private void sendConstantMessage(String chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage message = createSendMessage(
                chatId,
                text,
                replyKeyboard
        );
        telegramClient.execute(message);
    }

    public boolean isNeedInvoke(Map<String, Object> requestContext) {
        var authUser = AUTHENTICATED_USER.getValue(requestContext);
        return (!authUser.isRegistered())
                && (authUser.getPartnerAccount() == null);
    }

    public enum Steps {
        WAITING_FULLNAME,
        WAITING_REGION,
        WAITING_CONFIRMATION,
        NONE
    }

    public enum Callbacks {
        CONFIRM, EDIT_FULLNAME, EDIT_REGION;
    }

    private Steps getUserStep(Map<String, Object> requestContext) {
        final var stringUserState = userContextService.getUserContextParamValue(
                AUTHENTICATED_USER.getValue(requestContext),
                SCENARIO_STEP
        );
        return Optional
                .ofNullable(stringUserState)
                .map(Steps::valueOf)
                .orElse(Steps.NONE);
    }
}
