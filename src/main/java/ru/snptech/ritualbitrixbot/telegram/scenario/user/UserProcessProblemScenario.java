package ru.snptech.ritualbitrixbot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.ritualbitrixbot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.ritualbitrixbot.repository.DealRepository;
import ru.snptech.ritualbitrixbot.service.UserContextService;
import ru.snptech.ritualbitrixbot.telegram.scenario.AbstractScenario;
import ru.snptech.ritualbitrixbot.telegram.service.TelegramDealSenderService;

import java.util.Map;

import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CURRENT_DEAL_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;

@Component
@RequiredArgsConstructor
public class UserProcessProblemScenario extends AbstractScenario {
    private final TelegramDealSenderService telegramDealSenderService;
    private final DealRepository dealRepository;
    private final BitrixIntegrationService bitrixIntegrationService;
    private final UserContextService userContextService;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.getMessage().hasText()) {
            bitrixIntegrationService.problem(
                    userContextService.getUserContextParamValue(
                            AUTHENTICATED_USER.getValue(requestContext),
                            CURRENT_DEAL_ID
                    ),
                    tgUpdate.getMessage().getText()
            );
            telegramDealSenderService.notifyPartnerAndAssistantsAboutDealProblem(
                    AUTHENTICATED_USER.getValue(requestContext),
                    dealRepository.findDealById(
                            userContextService.getUserContextParamValue(
                                    AUTHENTICATED_USER.getValue(requestContext),
                                    CURRENT_DEAL_ID
                            )
                    ),
                    tgUpdate.getMessage().getText()
            );
            userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
        }
    }

}
