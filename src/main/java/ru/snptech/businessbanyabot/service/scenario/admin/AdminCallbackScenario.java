package ru.snptech.businessbanyabot.service.scenario.admin;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.model.common.AdminMessageConstants;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;
import ru.snptech.businessbanyabot.model.user.UserStatus;
import ru.snptech.businessbanyabot.repository.SurveyRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.BaseCallbackScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.service.util.TimeUtil;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Admin.ADMIN_SURVEY_ACCEPT_PREFIX;
import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Admin.ADMIN_SURVEY_DECLINE_PREFIX;
import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Slf4j
@Component
public class AdminCallbackScenario extends BaseCallbackScenario {

    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final UserContextService userContextService;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var callback = TG_UPDATE.getValue(requestContext).getCallbackQuery();
        var callbackPrefix = extractCallbackPrefix(callback.getData());
        var callbackPostfix = extractCallbackPostfix(callback.getData());

        switch (callbackPrefix) {
            case ADMIN_SURVEY_DECLINE_PREFIX, ADMIN_SURVEY_ACCEPT_PREFIX ->
                handleSurveyVerdict(requestContext, callbackPrefix, callbackPostfix);

        }

        releaseCallback(requestContext);
    }

    @SneakyThrows
    private void handleSurveyVerdict(
        Map<String, Object> requestContext,
        String callbackPrefix,
        String surveyId
    ) {
        var survey = surveyRepository.findById(Long.parseLong(surveyId))
            .orElseThrow(BusinessBanyaDomainLogicException.SURVEY_NOT_FOUND::new);

        var user = survey.getUser();
        var userContext = userContextService.getUserContext(user);

        switch (callbackPrefix) {
            case ADMIN_SURVEY_DECLINE_PREFIX -> {
                var bannedAt = Instant.now();

                user.setBannedAt(bannedAt);
                user.setStatus(UserStatus.BANNED);
                survey.setStatus(SurveyStatus.DECLINED);
                USER_STATUS.setValue(requestContext, UserStatus.BANNED);

                // TODO use value from env
                var bannedUntil = TimeUtil.formatToRussianDate(
                    bannedAt.plus(Duration.ofDays(93))
                );

                sendMessage(requestContext, AdminMessageConstants.SURVEY_DECLINED_MESSAGE.formatted(bannedUntil));
            }

            case ADMIN_SURVEY_ACCEPT_PREFIX -> {
                survey.setStatus(SurveyStatus.ACCEPTED);
                SCENARIO.setValue(userContext, ScenarioType.PAYMENT.name());
                IS_SURVEY_ACCEPTED.setValue(userContext, true);

                sendMessage(
                    userContext,
                    MessageConstants.SURVEY_ACCEPTED_CHOOSE_PAYMENT_METHOD,
                    MenuConstants.createChoosePaymentMethodMenu(user.getChatId())
                );

                sendMessage(requestContext, AdminMessageConstants.SURVEY_ACCEPT_MESSAGE);
            }
        }

        userContextService.updateUserContext(user, userContext);
        userRepository.save(user);
        surveyRepository.save(survey);
    }

    // TODO find ways to avoid constructor declaration by lombok
    public AdminCallbackScenario(
        UserRepository userRepository,
        SurveyRepository surveyRepository,
        UserContextService userContextService,
        TelegramClientAdapter telegramClientAdapter
    ) {
        super(telegramClientAdapter);

        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
    }
}
