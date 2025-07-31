package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.scenario.survey.SurveyScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;
import java.util.Set;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class UserMainMenuScenario extends AbstractScenario {

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

    public UserMainMenuScenario(
        TelegramClientAdapter telegramClientAdapter,
        UserContextService userContextService,
        UserRepository userRepository,
        SurveyScenario surveyScenario
    ) {
        super(telegramClientAdapter);

        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.surveyScenario = surveyScenario;
    }

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

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
                    sendMessage(
                        requestContext,
                        MessageConstants.MAIN_MENU,
                        MenuConstants.createUserMainMenu(user.getRole())
                    );
                }
            }
        }
    }

}
