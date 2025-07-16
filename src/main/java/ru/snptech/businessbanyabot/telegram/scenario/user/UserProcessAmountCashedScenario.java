package ru.snptech.businessbanyabot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.DealFlow;
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
public class UserProcessAmountCashedScenario extends AbstractScenario {
    private final TelegramDealSenderService telegramDealSenderService;
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
            if (deal.getFlow() == DealFlow.FUNERAL) {
                bitrixIntegrationService.commissionFuneral(deal.getId());
            } else {
                bitrixIntegrationService.commissionShop(deal.getId());
            }
            bitrixIntegrationService.updateDealCommission(
                    userContextService.getUserContextParamValue(
                            AUTHENTICATED_USER.getValue(requestContext),
                            CURRENT_DEAL_ID
                    ),
                    tgUpdate.getMessage().getText()
            );
            deal.setCommission(new BigDecimal(tgUpdate.getMessage().getText()));
            dealRepository.save(deal);
            dealService.finishDeal(deal.getId(), true);
            telegramDealSenderService.notifyPartnerAndAssistantsAboutAmountCashedEntered(
                    AUTHENTICATED_USER.getValue(requestContext),
                    deal.getId(),
                    tgUpdate.getMessage().getText()
            );
            telegramDealSenderService.notifyPartnerAndAssistantsAboutDealFinished(
                    AUTHENTICATED_USER.getValue(requestContext),
                    deal
            );
            userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
        }
    }

}
