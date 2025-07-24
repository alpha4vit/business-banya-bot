package ru.snptech.businessbanyabot.service.scenario.survey;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.entity.SurveyQuestion;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.event.SurveyCreatedEvent;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;
import ru.snptech.businessbanyabot.repository.SurveyQuestionRepository;
import ru.snptech.businessbanyabot.repository.SurveyRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
@RequiredArgsConstructor
public class SurveyScenario extends AbstractScenario {

    private final TelegramClientAdapter telegramClientAdapter;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyRepository surveyRepository;
    private final UserContextService userContextService;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    private List<SurveyQuestion> questions = Collections.emptyList();

    private long lastQuestionNumber = 0L;
    private static final long FIRST_MESSAGE_NUMBER = 1L;

    @Scheduled(initialDelay = 0L, fixedRate = 1L, timeUnit = TimeUnit.HOURS)
    private void updateQuestions() {
        questions = surveyQuestionRepository.getQuestions();

        lastQuestionNumber = questions.stream()
            .map(SurveyQuestion::getNumber)
            .max(Long::compare)
            .orElse(FIRST_MESSAGE_NUMBER);
    }

    public void invoke(Map<String, Object> requestContext) {
        var update = TG_UPDATE.getValue(requestContext);
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

        var surveyStep = SCENARIO_STEP.getValue(requestContext, SurveyScenarioStep.class);

        if (surveyStep.equals(SurveyScenarioStep.INITIAL)) {
            handleSurveyInit(requestContext, user);

            return;
        }

        var currentQuestion = getQuestionByFilter(
            (it) -> it.getScenarioStep().equals(surveyStep)
        );

        var survey = getOrCreateSurvey(user);
        var text = update.getMessage().getText();
        var updated = updateSurvey(survey, surveyStep, text);

        LATEST_SURVEY.setValue(requestContext, survey);

        if (currentQuestion.getNumber() == lastQuestionNumber) {
            surveyRepository.save(updated);

            handleSurveyComplete(requestContext, user, updated);

            return;
        }

        var nextQuestion = getQuestionByFilter(
            (it) -> it.getNumber().equals(currentQuestion.getNumber() + 1)
        );

        SCENARIO_STEP.setValue(requestContext, nextQuestion.getScenarioStep().name());

        sendQuestion(user, nextQuestion);

        userContextService.updateUserContext(user, requestContext);
        surveyRepository.save(updated);
    }

    private void handleSurveyInit(Map<String, Object> context, TelegramUser user) {
        var firstQuestion = getQuestionByFilter(
            (it) -> it.getNumber() == FIRST_MESSAGE_NUMBER
        );

        SCENARIO_STEP.setValue(context, firstQuestion.getScenarioStep().name());
        IS_SURVEY_ACCEPTED.setValue(context, false);

        sendQuestion(user, firstQuestion);

        userContextService.updateUserContext(user, context);
    }

    private void handleSurveyComplete(Map<String, Object> context, TelegramUser user, Survey survey) {
        telegramClientAdapter.sendMessage(user.getChatId(), MessageConstants.SURVEY_COMPLETE_MESSAGE);

        survey.setStatus(SurveyStatus.ON_MODERATION);

        surveyRepository.save(survey);
        userContextService.updateUserContext(user, context);

        eventPublisher.publishEvent(
            new SurveyCreatedEvent(
                user,
                survey
            )
        );
    }

    private Survey getOrCreateSurvey(TelegramUser user) {
        return surveyRepository.findFirstByUserChatIdOrderByCreatedAt(user.getChatId())
            .orElseGet(() -> {
                var created = new Survey();

                created.setUser(user);
                created.setStatus(SurveyStatus.IN_PROGRESS);

                return surveyRepository.save(created);
            });
    }

    private Survey updateSurvey(Survey survey, SurveyScenarioStep step, String value) {
        var updated = survey.updateField(step, value);

        return surveyRepository.save(updated);
    }

    private SurveyQuestion getQuestionByFilter(Predicate<SurveyQuestion> predicate) {
        return questions.stream()
            .filter(predicate)
            .findFirst()
            .orElseThrow(BusinessBanyaInternalException.SURVEY_QUESTION_NOT_FOUND::new);
    }


    @SneakyThrows
    private void sendQuestion(TelegramUser user, SurveyQuestion question) {
        telegramClientAdapter.sendMessage(user.getChatId(), question.getMessage());
    }
}
