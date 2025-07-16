package ru.snptech.businessbanyabot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.entity.DealActionStatus;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.businessbanyabot.repository.DealRepository;
import ru.snptech.businessbanyabot.service.DealService;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.telegram.service.TelegramDealSenderService;

import java.math.BigDecimal;
import java.util.Map;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.CURRENT_DEAL_ID;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;

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
