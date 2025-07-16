package ru.snptech.businessbanyabot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.businessbanyabot.repository.DealRepository;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.telegram.service.TelegramDealSenderService;

import java.util.Map;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.CURRENT_DEAL_ID;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;

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
