package ru.snptech.businessbanyabot.service.scenario.admin;

import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.notification.Notification;
import ru.snptech.businessbanyabot.model.scenario.step.NotificationScenarioStep;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.repository.specification.TelegramUserSpecification;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class AdminNotificationScenario extends AbstractScenario {

    private static final String USER_SEPARATOR = ",";
    private static final String LIST_SEPARATOR = "\n";

    private final UserRepository userRepository;
    private final UserContextService userContextService;

    public void invoke(Map<String, Object> requestContext) {
        var update = TG_UPDATE.getValue(requestContext);
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

        var step = SCENARIO_STEP.getValue(requestContext, NotificationScenarioStep.class);

        switch (step) {
            case INIT -> {
                SCENARIO_STEP.setValue(requestContext, NotificationScenarioStep.CONTENT_INPUT.name());

                sendMessage(
                    requestContext,
                    MessageConstants.NOTIFICATION_CONTENT_MESSAGE
                );

                userContextService.updateUserContext(user, requestContext);
            }

            case CONTENT_INPUT -> {
                if (!update.hasMessage()) {
                    throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
                }

                var content = update.getMessage().getText();

                var notification = Notification.builder()
                    .content(content)
                    .chatIds(Collections.emptySet())
                    .build();

                SCENARIO_STEP.setValue(requestContext, NotificationScenarioStep.CONSUMER_INPUT.name());
                NOTIFICATION.setValue(requestContext, notification);

                sendMessage(
                    requestContext,
                    MessageConstants.NOTIFICATION_CONSUMER_MESSAGE
                );

                userContextService.updateUserContext(user, requestContext);
            }

            case CONSUMER_INPUT -> {
                if (!update.hasMessage()) {
                    throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
                }

                var notification = NOTIFICATION.getValue(requestContext, Notification.class);

                var userFullNames = Arrays.stream(update.getMessage().getText()
                        .split(USER_SEPARATOR))
                    .map(String::trim)
                    .collect(Collectors.toSet());

                var users = userRepository.findAll(TelegramUserSpecification.nameContainsAny(userFullNames));

                var existedUsers = users.stream()
                    .map((it) -> it.getFullName().trim())
                    .collect(Collectors.toSet());

                notification.setChatIds(users.stream().map(TelegramUser::getChatId).collect(Collectors.toSet()));

                NOTIFICATION.setValue(requestContext, notification);

                userContextService.updateUserContext(user, requestContext);

                var notFound = userFullNames
                    .stream()
                    .filter((it) -> !existedUsers.contains(it))
                    .toList();

                var notFoundList = String.join(LIST_SEPARATOR, notFound);

                if (!notFound.isEmpty()) {
                    sendMessage(
                        requestContext,
                        MessageConstants.CONSUMERS_NOT_FOUND_BY_FULL_NAMES.formatted(notFoundList),
                        MenuConstants.createNotificationMenu(chatId)
                    );

                    return;
                }

                var toSend = userRepository.findByChatIdIn(notification.getChatIds());

                toSend.addAll(users);

                sendNotifications(notification, toSend);

                sendMessage(
                    requestContext,
                    MessageConstants.NOTIFICATION_SUCCESSFULLY_SENT
                );
            }

            case SEND -> {
                var notification = NOTIFICATION.getValue(requestContext, Notification.class);

                var users = userRepository.findByChatIdIn(notification.getChatIds());

                sendNotifications(notification, users);

                sendMessage(
                    requestContext,
                    MessageConstants.NOTIFICATION_SUCCESSFULLY_SENT
                );
            }
        }
    }

    private void sendNotifications(Notification notification, List<TelegramUser> users) {
        users.forEach((user) -> {
            sendMessage(
                userContextService.getUserContext(user),
                notification.getContent()
            );
        });
    }


    public AdminNotificationScenario(
        TelegramClientAdapter telegramClientAdapter,
        UserRepository userRepository,
        UserContextService userContextService
    ) {
        super(telegramClientAdapter);

        this.userContextService = userContextService;
        this.userRepository = userRepository;
    }
}
