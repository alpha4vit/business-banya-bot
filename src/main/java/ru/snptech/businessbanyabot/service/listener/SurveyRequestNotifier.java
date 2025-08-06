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
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import static ru.snptech.businessbanyabot.model.common.AdminMessageConstants.NEW_SURVEY_MESSAGE_TEMPLATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class SurveyRequestNotifier {

    private final TelegramClientAdapter telegramClientAdapter;

    @SneakyThrows
    @EventListener
    public void notifyAdminAboutSurveyComplete(SurveyCreatedEvent event) {
        log.info("Notifying admin about survey complete by user '{}'", event.user().getChatId());

        telegramClientAdapter.sendMessage(
            852874671L,
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
            survey.getSocialMedia()
        );
    }
}
