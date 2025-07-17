package ru.snptech.businessbanyabot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.scenario.AbstractScenario;

import java.util.Map;
import java.util.Optional;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.SCENARIO;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.USER_STATE;

@Component
@RequiredArgsConstructor
public class UserUpdateScenario extends AbstractScenario {
    private final UserCallbackScenario userCallbackScenario;
    private final UserMainMenuScenario userMainMenuScenario;
    private final UserContextService userContextService;

    public void invoke(Map<String, Object> requestContext) {
        if (TG_UPDATE.getValue(requestContext).hasCallbackQuery()) {
            userCallbackScenario.invoke(requestContext);
        } else if (TG_UPDATE.getValue(requestContext).hasMessage()) {
            var currentScenario = userContextService.getUserContextParamValue(
                AUTHENTICATED_USER.getValue(requestContext),
                    SCENARIO
            );
            if (
                    TG_UPDATE.getValue(requestContext).getMessage().hasText()
                            && "/start".equals(TG_UPDATE.getValue(requestContext).getMessage().getText())
                            && UserMainMenuScenario.MAIN_MENU_COMMANDS.contains(TG_UPDATE.getValue(requestContext).getMessage().getText())
            ) {
                userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
            }

            var currentState = Optional.ofNullable(
                    userContextService.getUserContextParamValue(
                        AUTHENTICATED_USER.getValue(requestContext),
                            USER_STATE
                    )
            ).map(UserState::valueOf).orElse(null);
            switch (currentState) {
                case null -> userMainMenuScenario.invoke(requestContext);
                case WAITING_AMOUNT -> {
//                    userProcessAmountScenario.invoke(requestContext);
                    userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
                }
                case WAITING_PROBLEM -> {
//                    userProcessProblemScenario.invoke(requestContext);
                    userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
                }
                case WAITING_AMOUNT_CASHED -> {
//                    userProcessAmountCashedScenario.invoke(requestContext);
                    userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
                }
            }
        }
    }
}
