package ru.snptech.businessbanyabot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.MenuConstants;
import ru.snptech.businessbanyabot.telegram.scenario.AbstractScenario;

import java.util.Map;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.SCENARIO;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.SCENARIO_STEP;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;

@Component
@RequiredArgsConstructor
public class AddPartnerScenario extends AbstractScenario {
    private final UserContextService userContextService;
    private final TelegramClient telegramClient;
    private final UserRepository userRepository;

    public void invoke(Map<String, Object> requestContext) {
        var currentStep = userContextService.getUserContextParamValue(
                AUTHENTICATED_USER.getValue(requestContext),
                SCENARIO_STEP
        );
        if (currentStep == null) {
            processScenarioInit(requestContext);
        } else {
            var tgUpdate = TG_UPDATE.getValue(requestContext);
            if (tgUpdate.hasMessage() && tgUpdate.getMessage().hasText()) {
                if ("Отменить".equals(tgUpdate.getMessage().getText())) {
                    processScenarioExit(requestContext);
                } else {
                    processScenarioEnter(requestContext);
                    userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
                }
            }
        }
    }

    @SneakyThrows
    private void processScenarioExit(Map<String, Object> requestContext) {
        userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
        sendMessageWithMainMenu(requestContext, "Главное меню");
    }

    @SneakyThrows
    private void sendMessageWithMainMenu(Map<String, Object> requestContext, String text) {
        telegramClient.execute(
                SendMessage.builder()
                        .chatId(CHAT_ID.getValue(requestContext))
                        .text(text)
                        .replyMarkup(MenuConstants.createUserMainMenu(AUTHENTICATED_USER.getValue(requestContext).getPartnerAccount() == null))
                        .build()
        );
    }

    @SneakyThrows
    private void processScenarioInit(Map<String, Object> requestContext) {
        userContextService.updateUserContext(
                AUTHENTICATED_USER.getValue(requestContext),
                SCENARIO_STEP,
                Steps.WAITING_USERNAME.name()
        );
        userContextService.updateUserContext(
                AUTHENTICATED_USER.getValue(requestContext),
                SCENARIO,
                "AddPartnerScenario"
        );
        sendMessageWithCancelMenu(requestContext, "Пришлите имя пользователя в телеграм ассистента");
    }

    @SneakyThrows
    private void sendMessageWithCancelMenu(Map<String, Object> requestContext, String text) {
        telegramClient.execute(
                SendMessage.builder()
                        .chatId(CHAT_ID.getValue(requestContext))
                        .text(text)
                        .replyMarkup(
                                ReplyKeyboardMarkup.builder()
                                        .resizeKeyboard(true)
                                        .keyboardRow(new KeyboardRow(new KeyboardButton("Отменить")))
                                        .build()
                        )
                        .build()
        );
    }

    private void processScenarioEnter(Map<String, Object> requestContext) {
        var text = TG_UPDATE.getValue(requestContext).getMessage().getText();
        if (text.contains("@")) text = text.replace("@", "");
        var telegramUser = userRepository.findByTelegramUsername(text);
        if (telegramUser == null) {
            sendMessageWithCancelMenu(requestContext, "Пользователь с таким Username не зашел в бота!");
            userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
            return;
        }
        if (telegramUser.getPartnerAccount() != null) {
            sendMessageWithCancelMenu(requestContext, "Пользователь с таким Username является ассистентом другого партнера!");
            userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
            return;
        }
        if (telegramUser.isActive()) {
            sendMessageWithCancelMenu(requestContext, "Пользователь с таким Username сам является партнером!");
            userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
            return;
        }
        telegramUser.setPartnerAccount(AUTHENTICATED_USER.getValue(requestContext));
        userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
        userRepository.save(telegramUser);
        sendMessageWithMainMenu(requestContext, "Пользователь успешно добавлен к вам ассистентом!");
    }

    public enum Steps {
        WAITING_USERNAME
    }
}
