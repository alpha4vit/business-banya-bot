package ru.snptech.businessbanyabot.service.scenario.search;

import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.scenario.step.SearchScenarioStep;
import ru.snptech.businessbanyabot.model.search.SearchMetadata;
import ru.snptech.businessbanyabot.model.search.SlideDirection;
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

                    return;
                }

                var messageId = sendUserPreviewCard(
                    null,
                    chatId,
                    requestContext,
                    matchingUsers.get(INITIAL_RESIDENT_SLIDER_INDEX)
                );

                SCENARIO_STEP.setValue(requestContext, SearchScenarioStep.RESIDENT_SLIDER.name());
                SEARCH_METADATA.setValue(requestContext, new SearchMetadata(searchString, INITIAL_RESIDENT_SLIDER_INDEX));
                MESSAGE_ID.setValue(requestContext, messageId);

                userContextService.updateUserContext(user, requestContext);
            }
        }
    }

    public void slideTo(SlideDirection direction, Map<String, Object> context) {
        var chatId = CHAT_ID.getValue(context, Long.class);
        var user = userRepository.findByChatId(chatId);
        var messageId = MESSAGE_ID.getValue(context);

        var searchMetadata = SEARCH_METADATA.getValue(context, SearchMetadata.class);

        var matchingUsers = userRepository.findByFullNameContainingIgnoreCase(searchMetadata.getSearchString());

        if (matchingUsers.isEmpty()) {
            sendMessage(
                context,
                MessageConstants.NO_RESIDENTS_FIND
            );
        }

        var nextCursor = direction.nextPosition(searchMetadata.getSliderCursor(), matchingUsers.size());

        sendUserPreviewCard(messageId, chatId, context, matchingUsers.get(nextCursor));

        searchMetadata.setSliderCursor(nextCursor);

        SEARCH_METADATA.setValue(context, searchMetadata);

        userContextService.updateUserContext(user, context);
    }

    private Integer sendUserPreviewCard(Integer messageId, Long chatId, Map<String, Object> context, TelegramUser user) {
        var message = MessageConstants.USER_CARD_PREVIEW.formatted(
            user.getFullName(),
            user.getPhoneNumber(),
            getOrEmpty(user.getInfo().getBusinessDescription()),
            getOrEmpty(user.getInfo().getMainActive())
        );

        var menu = MenuConstants.createResidentSliderMenu(chatId, user.getChatId());

        if (messageId == null) return sendMessage(context, message, menu);

        return updateMessage(messageId, context, message, menu);
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