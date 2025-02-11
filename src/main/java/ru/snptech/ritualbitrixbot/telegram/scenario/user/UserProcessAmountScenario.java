package ru.snptech.ritualbitrixbot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.entity.DealFlow;
import ru.snptech.ritualbitrixbot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.ritualbitrixbot.repository.DealRepository;
import ru.snptech.ritualbitrixbot.service.DealService;
import ru.snptech.ritualbitrixbot.service.UserContextService;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;
import ru.snptech.ritualbitrixbot.telegram.scenario.AbstractScenario;

import java.util.Map;

import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CURRENT_DEAL_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.USER_STATE;

@Component
@RequiredArgsConstructor
public class UserProcessAmountScenario extends AbstractScenario {
    private final TelegramClient telegramClient;
    private final BitrixIntegrationService bitrixIntegrationService;
    private final UserContextService userContextService;
    private final DealService dealService;
    private final DealRepository dealRepository;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.getMessage().hasText() && StringUtils.isNumeric(tgUpdate.getMessage().getText())) {
            var deal = dealRepository.findDealById(userContextService.getUserContextParamValue(
                    AUTHENTICATED_USER.getValue(requestContext),
                    CURRENT_DEAL_ID
            ));
            if (deal.getFlow() == DealFlow.SHOP) {
                bitrixIntegrationService.successDealShop(
                        userContextService.getUserContextParamValue(
                                AUTHENTICATED_USER.getValue(requestContext),
                                CURRENT_DEAL_ID
                        ),
                        tgUpdate.getMessage().getText()
                );
            } else if (deal.getFlow() == DealFlow.FUNERAL) {
                bitrixIntegrationService.successDealFuneral(
                        userContextService.getUserContextParamValue(
                                AUTHENTICATED_USER.getValue(requestContext),
                                CURRENT_DEAL_ID
                        ),
                        tgUpdate.getMessage().getText()
                );
            }
            telegramClient.execute(createSendMessage(
                    CHAT_ID.getValue(requestContext),
                    MessageConstants.WAITING_AMOUNT_CASHED_INPUT_MESSAGE
            ));
            userContextService.updateUserContext(
                    AUTHENTICATED_USER.getValue(requestContext),
                    USER_STATE,
                    UserState.WAITING_AMOUNT_CASHED.name()
            );
        }
    }

}
