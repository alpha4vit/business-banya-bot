package ru.snptech.businessbanyabot.service.scenario.admin;

import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.TG_UPDATE;

@Component
public class AdminUpdateScenario extends AbstractScenario {

    private final AdminCallbackScenario adminCallbackScenario;

    public AdminUpdateScenario(
        TelegramClientAdapter telegramClientAdapter,
        AdminCallbackScenario adminCallbackScenario
    ) {
        super(telegramClientAdapter);

        this.adminCallbackScenario = adminCallbackScenario;
    }

    public void invoke(Map<String, Object> requestContext) {
        if (TG_UPDATE.getValue(requestContext).hasCallbackQuery()) {
            adminCallbackScenario.invoke(requestContext);

            return;
        }

        if (!TG_UPDATE.getValue(requestContext).hasMessage()) {
            throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
        }

    }
}
