package ru.snptech.businessbanyabot.telegram.scenario;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.scenario.ScenarioType;
import ru.snptech.businessbanyabot.scenario.step.VerificationScenarioStep;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.MessageConstants;

import java.util.Map;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationScenario extends AbstractScenario {

    private final TelegramClient telegramClient;
    private final UserContextService userContextService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var user = AUTHENTICATED_USER.getValue(requestContext);

        var t = objectMapper.readValue(user.getContext(),  new TypeReference<Map<String, Object>>() {});

        if (IS_ADMIN.getValue(requestContext)) return;

        var isVerified = IS_VERIFIED.getValue(requestContext);

        if (Boolean.TRUE.equals(isVerified)) {
            SCENARIO.setValue(requestContext, ScenarioType.MAIN_MENU.name());
            userContextService.updateUserContext(user, requestContext);

            return;
        }

        if (VerificationScenarioStep.WAITING_NUMBER.equals(SCENARIO_STEP.getValue(requestContext))) {
            verifyPhoneNumber(requestContext, user);

            return;
        }

        telegramClient.execute(
            createSendMessage(
                String.valueOf(user.getChatId()),
                MessageConstants.VERIFICATION_NEED_MESSAGE
            )
        );

        IS_VERIFIED.setValue(requestContext, false);
        SCENARIO_STEP.setValue(requestContext, VerificationScenarioStep.WAITING_NUMBER);
        userContextService.updateUserContext(user, requestContext);
    }

    private void verifyPhoneNumber(Map<String, Object> context, TelegramUser user) {
        var message = TG_UPDATE.getValue(context).getMessage();

        if (!message.hasText() || !isPhoneNumber(message.getText())) {
            throw new IllegalArgumentException(MessageConstants.PHONE_NUMBER_IS_REQUIRED);
        }

        IS_VERIFIED.setValue(context, true);
        SCENARIO.setValue(context, ScenarioType.MAIN_MENU.name());
        SCENARIO_STEP.setValue(context, null);
        AUTHENTICATED_USER.setValue(context, user);
        userContextService.updateUserContext(user, context);
    }

    private Boolean isPhoneNumber(String text) {
        return text != null && text.matches("^\\+\\d{10,15}$");
    }
}
