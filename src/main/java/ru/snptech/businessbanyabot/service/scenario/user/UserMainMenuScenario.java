package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.entity.Role;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.repository.impl.JdbcUserRepository;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.service.scenario.common.AbstractScenario;
import ru.snptech.businessbanyabot.telegram.MenuConstants;
import ru.snptech.businessbanyabot.telegram.MessageConstants;

import java.util.Map;
import java.util.Set;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.*;

@Component
@RequiredArgsConstructor
public class UserMainMenuScenario extends AbstractScenario {
    private final TelegramClient telegramClient;

    public static final String SEARCH = "Поиск";
    public static final String BALANCE = "Мой баланс";
    public static final String EVENTS = "События";
    public static final String REQUEST = "Заявка";
    public static final String MAIN_MENU = "Главное меню";
    public static final Set<String> MAIN_MENU_COMMANDS = Set.of(
        SEARCH, BALANCE, EVENTS
    );
    private final UserContextService userContextService;
    private final JdbcUserRepository userRepository;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        var user = AUTHENTICATED_USER.getValue(requestContext);
        var role = USER_ROLE.getValue(requestContext, Role.class);

        if (tgUpdate.getMessage().hasText()) {
            switch (tgUpdate.getMessage().getText()) {
                case null -> {
                    throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
                }

                case REQUEST -> {
                    SCENARIO.setValue(requestContext, ScenarioType.SURVEY.name());
                    userContextService.updateUserContext(user, requestContext);
                }

                default -> {
                    telegramClient.execute(
                        createSendMessage(
                            CHAT_ID.getValue(requestContext),
                            MessageConstants.MAIN_MENU,
                            MenuConstants.createUserMainMenu(role)
                        ));
                }
            }
        }
    }

}
