package ru.snptech.businessbanyabot.service.scenario.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.service.scenario.user.UserMainMenuScenario;

import java.util.Map;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.*;

@Component
@RequiredArgsConstructor
public class UserUpdateScenario extends AbstractScenario {
    private final UserCallbackScenario userCallbackScenario;
    private final UserMainMenuScenario userMainMenuScenario;
    private final UserContextService userContextService;

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
            userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
        }

    }
}
