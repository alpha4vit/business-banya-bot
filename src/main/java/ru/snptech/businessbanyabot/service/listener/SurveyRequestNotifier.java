package ru.snptech.businessbanyabot.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.event.SurveyCreatedEvent;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

@Slf4j
@Component
@RequiredArgsConstructor
public class SurveyRequestNotifier {

    private final TelegramClientAdapter telegramClientAdapter;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @EventListener
    public void notifyAdminAboutSurveyComplete(SurveyCreatedEvent event) {
        log.info("Notifying admin about survey complete by user '{}'", event.user().getChatId());

        telegramClientAdapter.sendMessage(
            event.user().getChatId(),
            createMessage(event.user(), event.survey()),
            MenuConstants.createAdminSurveyAcceptMenu(event.survey())
        );
    }

    @SneakyThrows
    private String createMessage(TelegramUser user, Survey survey) {
        return MessageConstants.NEW_SURVEY_MESSAGE_TEMPLATE.formatted(
            survey.getId().toString(),
            survey.getFio(),
            survey.getActivityScope(),
            survey.getCompanyTurnover(),
            survey.getInterests()
        );
    }
}
