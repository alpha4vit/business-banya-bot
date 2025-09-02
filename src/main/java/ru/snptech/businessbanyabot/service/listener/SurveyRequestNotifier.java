package ru.snptech.businessbanyabot.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.event.SurveyCreatedEvent;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.repository.specification.UserSpecification;
import ru.snptech.businessbanyabot.service.util.TextUtils;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import static ru.snptech.businessbanyabot.model.common.AdminMessageConstants.NEW_SURVEY_MESSAGE_TEMPLATE;
import static ru.snptech.businessbanyabot.service.util.TextUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SurveyRequestNotifier {

    private final TelegramClientAdapter telegramClientAdapter;
    private final UserRepository userRepository;

    @SneakyThrows
    @EventListener
    public void notifyAdminAboutSurveyComplete(SurveyCreatedEvent event) {
        log.info("Notifying admin about survey complete by user '{}'", event.user().getChatId());

        var admins = userRepository.findAll(UserSpecification.hasRole(UserRole.ADMIN));

        telegramClientAdapter.sendMessage(
            admins.getFirst().getChatId(),
            createMessage(event.user(), event.survey()),
            MenuConstants.createAdminSurveyAcceptMenu(event.survey().getId())
        );
    }

    @SneakyThrows
    private String createMessage(TelegramUser user, Survey survey) {
        return NEW_SURVEY_MESSAGE_TEMPLATE.formatted(
            survey.getId().toString(),
            survey.getFio(),
            user.getPhoneNumber(),
            survey.getSocialMedia(),
            TG_USERNAME_TEMPLATE.formatted(user.getTelegramUsername()),
            user.getChatId(),
            WHATSAPP_TEMPLATE.formatted(phoneWithoutPlus(user.getPhoneNumber()))
        );
    }

    private String trimPlus(String value) {
        return value.replace("+", "");
    }
}
