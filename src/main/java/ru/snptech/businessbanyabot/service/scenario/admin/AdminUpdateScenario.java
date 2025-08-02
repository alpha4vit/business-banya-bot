package ru.snptech.businessbanyabot.service.scenario.admin;

import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.scenario.user.UserMainMenuScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class AdminUpdateScenario extends AbstractScenario {

    private final AdminCallbackScenario adminCallbackScenario;
    private final UserRepository userRepository;
    private final AdminMainMenuScenario adminMainMenuScenario;
    private final UserContextService userContextService;

    public AdminUpdateScenario(
        TelegramClientAdapter telegramClientAdapter,
        AdminCallbackScenario adminCallbackScenario,
        UserRepository userRepository,
        AdminMainMenuScenario adminMainMenuScenario,
        UserContextService userContextService
    ) {
        super(telegramClientAdapter);

        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.adminMainMenuScenario = adminMainMenuScenario;
        this.adminCallbackScenario = adminCallbackScenario;
    }

    public void invoke(Map<String, Object> requestContext) {
        var update = TG_UPDATE.getValue(requestContext);
        var chatId = CHAT_ID.getValue(requestContext, Long.class);

        if (update.hasCallbackQuery()) {
            adminCallbackScenario.invoke(requestContext);

            return;
        }

        if (!update.hasMessage()) {
            throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
        }

        cleanContextIfNeeded(chatId, requestContext);

        var currentScenario = SCENARIO.getValue(requestContext, ScenarioType.class);


        switch (currentScenario) {
            case MAIN_MENU -> {
                adminMainMenuScenario.invoke(requestContext);
            }

            default -> {
                // nothing for now
            }
        }
    }

    private void cleanContextIfNeeded(Long chatId, Map<String, Object> requestContext) {
        if (
            TG_UPDATE.getValue(requestContext).getMessage().hasText()
                && "/start".equals(TG_UPDATE.getValue(requestContext).getMessage().getText())
                && UserMainMenuScenario.MAIN_MENU_COMMANDS.contains(TG_UPDATE.getValue(requestContext).getMessage().getText())
        ) {
            var user = userRepository.findByChatId(chatId);

            requestContext.clear();

            SCENARIO.setValue(requestContext, ScenarioType.MAIN_MENU.name());

            userContextService.updateUserContext(user, requestContext);
        }
    }
}
