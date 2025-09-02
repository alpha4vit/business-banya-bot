package ru.snptech.businessbanyabot.service.scenario.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.model.common.AdminMessageConstants;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.payment.PaymentMetadata;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.scenario.step.NotificationScenarioStep;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;
import ru.snptech.businessbanyabot.model.user.UserStatus;
import ru.snptech.businessbanyabot.propterties.ApplicationProperties;
import ru.snptech.businessbanyabot.repository.SurveyRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.BaseCallbackScenario;
import ru.snptech.businessbanyabot.service.scenario.report.ReportScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.service.util.TimeUtils;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Admin.*;
import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Slf4j
@Component
public class AdminCallbackScenario extends BaseCallbackScenario {

    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final ReportScenario reportScenario;
    private final UserContextService userContextService;
    private final ApplicationProperties applicationProperties;
    private final AdminNotificationScenario adminNotificationScenario;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var callback = TG_UPDATE.getValue(requestContext).getCallbackQuery();
        var callbackPrefix = extractCallbackPrefix(callback.getData());
        var callbackPostfix = extractCallbackPostfix(callback.getData());

        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

        switch (callbackPrefix) {
            case ADMIN_SURVEY_DECLINE_PREFIX, ADMIN_SURVEY_ACCEPT_PREFIX ->
                handleSurveyVerdict(requestContext, callbackPrefix, callbackPostfix);

            case ADMIN_SEND_NOTIFICATIONS, ADMIN_SEND_FOR_ALL_NOTIFICATIONS -> {
                SCENARIO_STEP.setValue(requestContext, NotificationScenarioStep.SEND.name());

                userContextService.updateUserContext(user, requestContext);

                adminNotificationScenario.invoke(requestContext);
            }

            case ADMIN_SEND_FOR_ADMIN_NOTIFICATIONS -> {
                SCENARIO_STEP.setValue(requestContext, NotificationScenarioStep.NOTIFY_ADMINS.name());

                userContextService.updateUserContext(user, requestContext);

                adminNotificationScenario.invoke(requestContext);
            }

            case ADMIN_SEND_FOR_MODERATOR_NOTIFICATIONS -> {
                SCENARIO_STEP.setValue(requestContext, NotificationScenarioStep.NOTIFY_MODERATORS.name());

                userContextService.updateUserContext(user, requestContext);

                adminNotificationScenario.invoke(requestContext);
            }

            case ADMIN_SEND_FOR_COORDINATOR_NOTIFICATIONS -> {
                SCENARIO_STEP.setValue(requestContext, NotificationScenarioStep.NOTIFY_COORDINATORS.name());

                userContextService.updateUserContext(user, requestContext);

                adminNotificationScenario.invoke(requestContext);
            }

            case REPORT_TYPE_PREFIX, REPORT_TYPE_PARAM_PREFIX -> reportScenario.invoke(requestContext, callbackPostfix);
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
                var bannedUntil = TimeUtils.formatToRussianDate(
                    bannedAt.plus(Duration.ofDays(93))
                );

                sendMessage(requestContext, AdminMessageConstants.SURVEY_DECLINED_MESSAGE.formatted(bannedUntil));
            }

            case ADMIN_SURVEY_ACCEPT_PREFIX -> {
                survey.setStatus(SurveyStatus.ACCEPTED);
                SCENARIO.setValue(userContext, ScenarioType.PAYMENT.name());
                IS_SURVEY_ACCEPTED.setValue(userContext, true);

                var paymentMetadata = objectMapper.writeValueAsString(
                    new PaymentMetadata(
                        applicationProperties.getPayment().getAmount(),
                        applicationProperties.getSubscriptionContinuationDurationInMonths()
                    )
                );

                sendMessage(
                    userContext,
                    MessageConstants.SURVEY_ACCEPTED_CHOOSE_PAYMENT_METHOD,
                    MenuConstants.createChoosePaymentMethodMenu(
                        paymentMetadata,
                        applicationProperties.getDeposit().getLegalEntityLink()
                    )
                );

                sendMessage(requestContext, AdminMessageConstants.SURVEY_ACCEPT_MESSAGE);
            }
        }

        userContextService.updateUserContext(user, userContext);
        userRepository.save(user);
        surveyRepository.save(survey);
    }

    public AdminCallbackScenario(
        UserRepository userRepository,
        SurveyRepository surveyRepository,
        ReportScenario reportScenario,
        UserContextService userContextService,
        TelegramClientAdapter telegramClientAdapter,
        ApplicationProperties applicationProperties,
        AdminNotificationScenario adminNotificationScenario,
        ObjectMapper objectMapper
    ) {
        super(telegramClientAdapter);

        this.reportScenario = reportScenario;
        this.applicationProperties = applicationProperties;
        this.adminNotificationScenario = adminNotificationScenario;
        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.objectMapper = objectMapper;
    }
}
