package ru.snptech.businessbanyabot.service.scenario.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.SurveyScenario;
import ru.snptech.businessbanyabot.service.scenario.user.UserMainMenuScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
@RequiredArgsConstructor
public class UserUpdateScenario extends AbstractScenario {
    private final UserCallbackScenario userCallbackScenario;
    private final UserMainMenuScenario userMainMenuScenario;
    private final SurveyScenario surveyScenario;
    private final UserContextService userContextService;
    private final UserRepository userRepository;

    public void invoke(Map<String, Object> requestContext) {
        if (TG_UPDATE.getValue(requestContext).hasCallbackQuery()) {
            userCallbackScenario.invoke(requestContext);

            return;
        }

        if (!TG_UPDATE.getValue(requestContext).hasMessage()) {
            throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
        }

        var currentScenario = SCENARIO.getValue(requestContext, ScenarioType.class);

        cleanContextIfNeeded(requestContext);

        switch (currentScenario) {
            case MAIN_MENU -> {
                userMainMenuScenario.invoke(requestContext);
            }

            case SURVEY -> {
                surveyScenario.invoke(requestContext);
            }

            default -> {
                // nothing for now
            }
        }
    }

    private void cleanContextIfNeeded(Map<String, Object> requestContext) {
        if (
            TG_UPDATE.getValue(requestContext).getMessage().hasText()
                && "/start".equals(TG_UPDATE.getValue(requestContext).getMessage().getText())
                && UserMainMenuScenario.MAIN_MENU_COMMANDS.contains(TG_UPDATE.getValue(requestContext).getMessage().getText())
        ) {
            var chatId = CHAT_ID.getValue(requestContext, Long.class);
            var user = userRepository.findByChatId(chatId);

            userContextService.cleanUserContext(user);
        }

    }
}
