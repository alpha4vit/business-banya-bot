package ru.snptech.businessbanyabot.service.scenario.search;

import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.scenario.step.SearchScenarioStep;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class SearchScenario extends AbstractScenario {

    private final UserContextService userContextService;
    private final UserRepository userRepository;

    private static final Integer INITIAL_RESIDENT_SLIDER_INDEX = 0;

    public void invoke(Map<String, Object> requestContext) {
        var update = TG_UPDATE.getValue(requestContext);
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

        var searchStep = SCENARIO_STEP.getValue(requestContext, SearchScenarioStep.class);

        switch (searchStep) {
            case INIT -> {
                SCENARIO_STEP.setValue(requestContext, SearchScenarioStep.SEARCH_INPUT.name());

                sendMessage(
                    requestContext,
                    MessageConstants.SEARCH_MENU_MESSAGE
                );

                userContextService.updateUserContext(user, requestContext);
            }

            case SEARCH_INPUT -> {
                if (!update.hasMessage()) {
                    throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
                }

                var searchString = update.getMessage().getText();
                var matchingUsers = userRepository.findByFullNameContainingIgnoreCase(searchString);

                if (matchingUsers.isEmpty()) {
                    sendMessage(
                        requestContext,
                        MessageConstants.NO_RESIDENTS_FIND
                    );
                }

                SCENARIO_STEP.setValue(requestContext, SearchScenarioStep.RESIDENT_SLIDER.name());
                RESIDENT_SLIDER_CURSOR.setValue(requestContext, INITIAL_RESIDENT_SLIDER_INDEX + 1);

                sendUserPreviewCard(requestContext, matchingUsers.get(INITIAL_RESIDENT_SLIDER_INDEX));
            }

            case RESIDENT_SLIDER -> {

            }
        }

    }

    private void sendUserPreviewCard(Map<String, Object> context, TelegramUser user) {
        var message = MessageConstants.USER_CARD_PREVIEW.formatted(
            user.getFullName(),
            user.getPhoneNumber(),
            getOrEmpty(user.getInfo().businessDescription()),
            getOrEmpty(user.getInfo().mainActive())
        );

        sendMessage(context, message);
    }

    public SearchScenario(
        TelegramClientAdapter telegramClientAdapter,
        UserContextService userContextService,
        UserRepository userRepository
    ) {
        super(telegramClientAdapter);

        this.userContextService = userContextService;
        this.userRepository = userRepository;
    }

    private String getOrEmpty(String value) {
        if (value == null) return "";

        return value;
    }
}