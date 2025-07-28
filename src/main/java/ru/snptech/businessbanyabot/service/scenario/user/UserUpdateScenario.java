package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.scenario.survey.SurveyScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class UserUpdateScenario extends AbstractScenario {

    private final UserCallbackScenario userCallbackScenario;
    private final UserMainMenuScenario userMainMenuScenario;
    private final SurveyScenario surveyScenario;
    private final UserContextService userContextService;
    private final UserRepository userRepository;
    private final PaymentScenario paymentScenario;

    public UserUpdateScenario(
        UserCallbackScenario userCallbackScenario,
        UserMainMenuScenario userMainMenuScenario,
        SurveyScenario surveyScenario,
        UserContextService userContextService,
        UserRepository userRepository,
        TelegramClientAdapter telegramClientAdapter,
        PaymentScenario paymentScenario
    ) {
        super(telegramClientAdapter);

        this.paymentScenario = paymentScenario;
        this.userCallbackScenario = userCallbackScenario;
        this.userMainMenuScenario = userMainMenuScenario;
        this.surveyScenario = surveyScenario;
        this.userContextService = userContextService;
        this.userRepository = userRepository;
    }

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        if (TG_UPDATE.getValue(requestContext).hasCallbackQuery()) {
            userCallbackScenario.invoke(requestContext);

            return;
        }

        if (!TG_UPDATE.getValue(requestContext).hasMessage()) {
            throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
        }

        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var currentScenario = SCENARIO.getValue(requestContext, ScenarioType.class);

        cleanContextIfNeeded(chatId, requestContext);

        switch (currentScenario) {
            case MAIN_MENU -> {
                userMainMenuScenario.invoke(requestContext);
            }

            case SURVEY -> {
                surveyScenario.invoke(requestContext);
            }

            // TODO use payment scenario instead
            case PAYMENT -> {
                var paymentType = PAYMENT_TYPE.getValue(requestContext, PaymentType.class);

                paymentScenario.handle(paymentType, requestContext);
            }

            default -> {
                // nothing for now
            }
        }
    }

    private void cleanContextIfNeeded(Long chatId, Map<String, Object> requestContext) {
        if (
            TG_UPDATE.getValue(requestContext).getMessage().hasText()
                && "/start".equals(TG_UPDATE.getValue(requestContext).getMessage().getText())
                && UserMainMenuScenario.MAIN_MENU_COMMANDS.contains(TG_UPDATE.getValue(requestContext).getMessage().getText())
        ) {
            var user = userRepository.findByChatId(chatId);

            userContextService.cleanUserContext(user);
        }
    }
}
