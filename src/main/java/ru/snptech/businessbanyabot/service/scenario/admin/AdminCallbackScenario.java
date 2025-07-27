package ru.snptech.businessbanyabot.service.scenario.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.integrations.bank.dto.common.QrType;
import ru.snptech.businessbanyabot.integrations.bank.service.BankIntegrationService;
import ru.snptech.businessbanyabot.model.common.AdminMessageConstants;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;
import ru.snptech.businessbanyabot.model.user.UserStatus;
import ru.snptech.businessbanyabot.repository.SurveyRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.BaseCallbackScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.service.util.ImageUtil;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.time.Instant;
import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Admin.ADMIN_SURVEY_ACCEPT_PREFIX;
import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Admin.ADMIN_SURVEY_DECLINE_PREFIX;
import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Slf4j
@Component
public class AdminCallbackScenario extends BaseCallbackScenario {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final UserContextService userContextService;
    private final BankIntegrationService bankIntegrationService;

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
                user.setBannedAt(Instant.now());
                user.setStatus(UserStatus.BANNED);
                survey.setStatus(SurveyStatus.DECLINED);
                USER_STATUS.setValue(requestContext, UserStatus.BANNED);

                sendMessage(requestContext, AdminMessageConstants.SURVEY_DECLINED_MESSAGE);
            }

            case ADMIN_SURVEY_ACCEPT_PREFIX -> {
                var bankResponse = bankIntegrationService.registerQrCode(QrType.DYNAMIC);

                var base64QrCode = bankResponse.data().image().content();
                var file = ImageUtil.decodeBase64ToFile(base64QrCode);

                user.setQrCode(base64QrCode);
                survey.setStatus(SurveyStatus.ACCEPTED);
                SCENARIO.setValue(userContext, ScenarioType.PAYMENT.name());
                IS_SURVEY_ACCEPTED.setValue(userContext, true);
                QR_CODE.setValue(userContext, base64QrCode);

                sendFile(requestContext, file);
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
        TelegramClientAdapter telegramClientAdapter,
        BankIntegrationService bankIntegrationService,
        ObjectMapper objectMapper
    ) {
        super(telegramClientAdapter);

        this.objectMapper = objectMapper;
        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.bankIntegrationService = bankIntegrationService;
    }
}
