package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.survey.SurveyScenario;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;

import java.util.Map;
import java.util.Set;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

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
    private final UserRepository userRepository;
    private final SurveyScenario surveyScenario;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);
        var role = USER_ROLE.getValue(requestContext, UserRole.class);

        if (tgUpdate.getMessage().hasText()) {
            switch (tgUpdate.getMessage().getText()) {
                case null -> {
                    throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
                }

                case REQUEST -> {
                    SCENARIO.setValue(requestContext, ScenarioType.SURVEY.name());
                    SCENARIO_STEP.setValue(requestContext, SurveyScenarioStep.INITIAL.name());
                    userContextService.updateUserContext(user, requestContext);

                    surveyScenario.invoke(requestContext);
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
