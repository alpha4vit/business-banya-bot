package ru.snptech.ritualbitrixbot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.ritualbitrixbot.service.UserContextService;
import ru.snptech.ritualbitrixbot.telegram.scenario.AbstractScenario;

import java.util.Map;
import java.util.Optional;

import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.USER_STATE;

@Component
@RequiredArgsConstructor
public class UserUpdateScenario extends AbstractScenario {
    private final UserCallbackScenario userCallbackScenario;
    private final UserMainMenuScenario userMainMenuScenario;
    private final UserProcessAmountScenario userProcessAmountScenario;
    private final UserProcessAmountCashedScenario userProcessAmountCashedScenario;
    private final UserProcessProblemScenario userProcessProblemScenario;
    private final UserContextService userContextService;

    public void invoke(Map<String, Object> requestContext) {
        if (TG_UPDATE.getValue(requestContext).hasCallbackQuery()) {
            userCallbackScenario.invoke(requestContext);
        } else if (TG_UPDATE.getValue(requestContext).hasMessage()) {
            var currentState = Optional.ofNullable(
                    userContextService.getUserContextParamValue(
                            AUTHENTICATED_USER.getValue(requestContext),
                            USER_STATE
                    )
            ).map(UserState::valueOf).orElse(null);
            switch (currentState) {
                case null -> userMainMenuScenario.invoke(requestContext);
                case WAITING_AMOUNT -> {
                    userProcessAmountScenario.invoke(requestContext);
                }
                case WAITING_PROBLEM -> {
                    userProcessProblemScenario.invoke(requestContext);
                }
                case WAITING_AMOUNT_CASHED -> {
                    userProcessAmountCashedScenario.invoke(requestContext);
                }
            }
        }
    }
}
