package ru.snptech.businessbanyabot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.repository.DealRepository;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.scenario.AbstractScenario;

import java.util.Map;
import java.util.Optional;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.SCENARIO_STEP;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;

@Component
@RequiredArgsConstructor
public class ShopDealScenario extends AbstractScenario {
    private final TelegramClient telegramClient;
    private final UserContextService userContextService;
    private final DealRepository dealRepository;

    public static final String USER_SHOP_APPROVE_CALLBACK_PREFIX = "USA_";
    public static final String USER_SHOP_REJECT_CALLBACK_PREFIX = "USR_";
    public static final String USER_SHOP_SECOND_APPROVE_CALLBACK_PREFIX = "USSA_";
    public static final String USER_SHOP_SECOND_REJECT_CALLBACK_PREFIX = "USSR_";

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        if (TG_UPDATE.contains(requestContext)) {
            var tgUpdate = TG_UPDATE.getValue(requestContext);
            if (tgUpdate.hasCallbackQuery()) {
                var tgCallbackData = tgUpdate.getCallbackQuery().getData();
                if (tgCallbackData.startsWith(USER_SHOP_APPROVE_CALLBACK_PREFIX)) {

                } else if (tgCallbackData.startsWith(USER_SHOP_REJECT_CALLBACK_PREFIX)) {

                } else if (tgCallbackData.startsWith(USER_SHOP_SECOND_APPROVE_CALLBACK_PREFIX)) {

                } else if (tgCallbackData.startsWith(USER_SHOP_SECOND_REJECT_CALLBACK_PREFIX)) {

                }
            }
        }
    }

    public enum Steps {
        WAITING_AMOUNT,
        WAITING_PROBLEM,
        WAITING_DECLINE,
        WAITING_AMOUNT_CASHED
    }

    private Steps getStep(Map<String, Object> requestContext) {
        return Optional.ofNullable(userContextService.getUserContextParamValue(AUTHENTICATED_USER.getValue(requestContext), SCENARIO_STEP))
                .map(Steps::valueOf)
                .orElse(null);
    }

}
