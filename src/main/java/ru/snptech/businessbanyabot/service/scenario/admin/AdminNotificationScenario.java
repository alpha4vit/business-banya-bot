package ru.snptech.businessbanyabot.service.scenario.admin;

import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.notification.Notification;
import ru.snptech.businessbanyabot.model.scenario.step.NotificationScenarioStep;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.repository.specification.UserSpecification;
import ru.snptech.businessbanyabot.service.notification.NotificationService;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.*;
import java.util.stream.Collectors;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class AdminNotificationScenario extends AbstractScenario {

    private static final String USER_SEPARATOR = ",";
    private static final String LIST_SEPARATOR = "\n";

    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final NotificationService notificationService;

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
                    MessageConstants.NOTIFICATION_CONSUMER_MESSAGE,
                    MenuConstants.createSendNotificationParamsMenu(chatId)
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

                Map<String, List<TelegramUser>> usersBySearch = userFullNames.stream()
                    .map(search -> Map.entry(search, findMatchingUsersByPhoneOrFullName(search)))
                    .filter(entry -> !entry.getValue().isEmpty())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                var existed = usersBySearch.values().stream()
                    .flatMap(Collection::stream)
                    .toList();

                notification.setChatIds(existed.stream().map(TelegramUser::getChatId).collect(Collectors.toSet()));

                NOTIFICATION.setValue(requestContext, notification);

                userContextService.updateUserContext(user, requestContext);

                var notFound = userFullNames
                    .stream()
                    .filter((it) -> !usersBySearch.containsKey(it))
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

                notificationService.sendNotification(notification, toSend);

                sendMessage(
                    requestContext,
                    MessageConstants.NOTIFICATION_SUCCESSFULLY_SENT
                );
            }

            case SEND -> {
                var notification = NOTIFICATION.getValue(requestContext, Notification.class);

                var users = userRepository.findByChatIdIn(notification.getChatIds());

                if (users.isEmpty()) {
                    users = userRepository.findAll(UserSpecification.hasRole(UserRole.RESIDENT));
                }

                notificationService.sendNotification(notification, users);

                sendMessage(
                    requestContext,
                    MessageConstants.NOTIFICATION_SUCCESSFULLY_SENT
                );
            }

            case NOTIFY_ADMINS -> {
                var users = userRepository.findAll(UserSpecification.hasRole(UserRole.ADMIN));

                sendNotification(requestContext, users);
            }

            case NOTIFY_MODERATORS -> {
                var users = userRepository.findAll(UserSpecification.hasRole(UserRole.MODERATOR));

                sendNotification(requestContext, users);
            }

            case NOTIFY_COORDINATORS -> {
                var users = userRepository.findAll(UserSpecification.hasRole(UserRole.COORDINATOR));

                sendNotification(requestContext, users);
            }
        }
    }

    private void sendNotification(Map<String, Object> context, List<TelegramUser> users) {
        var notification = NOTIFICATION.getValue(context, Notification.class);

        notificationService.sendNotification(notification, users);

        sendMessage(
            context,
            MessageConstants.NOTIFICATION_SUCCESSFULLY_SENT
        );
    }

    public AdminNotificationScenario(
        TelegramClientAdapter telegramClientAdapter,
        UserRepository userRepository,
        UserContextService userContextService,
        NotificationService notificationService
    ) {
        super(telegramClientAdapter);

        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    private List<TelegramUser> findMatchingUsersByPhoneOrFullName(String search) {
        var matchingUsers = userRepository.findByFullNameContainingIgnoreCase(search);
        matchingUsers.addAll(userRepository.findByPhoneNumberContainingIgnoreCase(search));

        return matchingUsers;
    }
}
