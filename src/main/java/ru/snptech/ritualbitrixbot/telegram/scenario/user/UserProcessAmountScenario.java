package ru.snptech.ritualbitrixbot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.entity.DealActionStatus;
import ru.snptech.ritualbitrixbot.entity.DealFlow;
import ru.snptech.ritualbitrixbot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.ritualbitrixbot.repository.DealRepository;
import ru.snptech.ritualbitrixbot.service.DealService;
import ru.snptech.ritualbitrixbot.service.UserContextService;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;
import ru.snptech.ritualbitrixbot.telegram.scenario.AbstractScenario;
import ru.snptech.ritualbitrixbot.telegram.service.TelegramDealSenderService;

import java.math.BigDecimal;
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
    private final TelegramDealSenderService telegramDealSenderService;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.getMessage().hasText() && StringUtils.isNumeric(tgUpdate.getMessage().getText())) {
            var deal = dealRepository.findDealById(userContextService.getUserContextParamValue(
                    AUTHENTICATED_USER.getValue(requestContext),
                    CURRENT_DEAL_ID
            ));
            bitrixIntegrationService.updateDealAmount(
                    userContextService.getUserContextParamValue(
                            AUTHENTICATED_USER.getValue(requestContext),
                            CURRENT_DEAL_ID
                    ),
                    tgUpdate.getMessage().getText()
            );
            deal.setDealActionStatus(DealActionStatus.WAITING_COMMISSION);
            deal.setAmount(new BigDecimal(tgUpdate.getMessage().getText()));
            deal = dealRepository.save(deal);
            telegramDealSenderService.notifyPartnerAndAssistantsAboutAmountEntered(
                    AUTHENTICATED_USER.getValue(requestContext),
                    userContextService.getUserContextParamValue(
                            AUTHENTICATED_USER.getValue(requestContext),
                            CURRENT_DEAL_ID
                    ),
                    tgUpdate.getMessage().getText()
            );
            userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
        }
    }

}
