package ru.snptech.businessbanyabot.service.scenario;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.scenario.step.VerificationScenarioStep;
import ru.snptech.businessbanyabot.model.user.Role;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.common.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationScenario extends AbstractScenario {

    private final TelegramClientAdapter telegramClientAdapter;
    private final UserContextService userContextService;
    private final UserRepository userRepository;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

        if (Role.ADMIN.equals(USER_ROLE.getValue(requestContext))) return;

        var isVerified = IS_VERIFIED.getValue(requestContext);

        if (Boolean.TRUE.equals(isVerified)) {
            if (SCENARIO.getValue(requestContext) == null) {
                SCENARIO.setValue(requestContext, ScenarioType.MAIN_MENU.name());
                userContextService.updateUserContext(user, requestContext);
            }

            return;
        }

        if (VerificationScenarioStep.WAITING_NUMBER.equals(SCENARIO_STEP.getValue(requestContext))) {
            verifyPhoneNumber(requestContext, user);

            return;
        }

        telegramClientAdapter.sendMessage(user.getChatId(), MessageConstants.VERIFICATION_NEED_MESSAGE);

        IS_VERIFIED.setValue(requestContext, false);
        SCENARIO_STEP.setValue(requestContext, VerificationScenarioStep.WAITING_NUMBER);
        userContextService.updateUserContext(user, requestContext);
    }

    private void verifyPhoneNumber(Map<String, Object> context, TelegramUser user) {
        var message = TG_UPDATE.getValue(context).getMessage();

        if (!message.hasText() || !isPhoneNumber(message.getText())) {
            throw new BusinessBanyaDomainLogicException.PHONE_NUMBER_IS_REQUIRED();
        }

        IS_VERIFIED.setValue(context, true);
        SCENARIO.setValue(context, ScenarioType.MAIN_MENU.name());
        userContextService.updateUserContext(user, context);
    }

    private Boolean isPhoneNumber(String text) {
        return text != null && text.matches("^\\+\\d{10,15}$");
    }
}
