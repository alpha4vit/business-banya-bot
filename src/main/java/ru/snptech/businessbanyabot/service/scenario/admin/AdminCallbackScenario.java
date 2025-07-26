package ru.snptech.businessbanyabot.service.scenario.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.integrations.bank.client.FeignBankClient;
import ru.snptech.businessbanyabot.integrations.bank.dto.common.QrType;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.QRCodeRequestParams;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.RegisterPaymentQrCodeRequest;
import ru.snptech.businessbanyabot.model.common.AdminMessageConstants;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;
import ru.snptech.businessbanyabot.model.user.UserStatus;
import ru.snptech.businessbanyabot.repository.SurveyRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.BaseCallbackScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.time.Instant;
import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Admin.ADMIN_SURVEY_ACCEPT_PREFIX;
import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Admin.ADMIN_SURVEY_DECLINE_PREFIX;
import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class AdminCallbackScenario extends BaseCallbackScenario {

    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final UserContextService userContextService;
    private final ObjectMapper objectMapper;
    private final FeignBankClient bankClient;

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

        switch (callbackPrefix) {
            case ADMIN_SURVEY_DECLINE_PREFIX -> {
                user.setBannedAt(Instant.now());
                user.setStatus(UserStatus.BANNED);
                survey.setStatus(SurveyStatus.DECLINED);
                USER_STATUS.setValue(requestContext, UserStatus.BANNED);

                sendMessage(requestContext, AdminMessageConstants.SURVEY_DECLINED_MESSAGE);
            }

            case ADMIN_SURVEY_ACCEPT_PREFIX -> {
                survey.setStatus(SurveyStatus.ACCEPTED);
                SCENARIO.setValue(requestContext, ScenarioType.PAYMENT.name());
                IS_SURVEY_ACCEPTED.setValue(requestContext, true);


                var bankResponse = bankClient.registerQrCode(
                    "v1.0",
                    "fbff76236132043e85f2524ad59524b8",
                    "40817810802000000008/044525104",
                    new RegisterPaymentQrCodeRequest(
                        10,
                        "BYN",
                        "test",
                        QrType.DYNAMIC.getValue(),
                        new QRCodeRequestParams(
                            "100",
                            "100",
                            "image/png"
                        ),
                        "test-source",
                        100

                    )
                );

                sendMessage(requestContext, AdminMessageConstants.SURVEY_ACCEPT_MESSAGE);
                sendMessage(requestContext, objectMapper.writeValueAsString(bankResponse));
            }
        }

        userContextService.updateUserContext(user, requestContext);
        userRepository.save(user);
        surveyRepository.save(survey);
    }

    // TODO find ways to avoid constructor declaration by lombok
    public AdminCallbackScenario(
        UserRepository userRepository,
        SurveyRepository surveyRepository,
        UserContextService userContextService,
        TelegramClientAdapter telegramClientAdapter,
        FeignBankClient bankClient,
        ObjectMapper objectMapper
    ) {
        super(telegramClientAdapter);

        this.objectMapper = objectMapper;
        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.bankClient = bankClient;
    }
}
