package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.model.common.CallbackPrefixes;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.model.scenario.step.EventScenarioStep;
import ru.snptech.businessbanyabot.model.search.SlideDirection;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.scenario.BaseCallbackScenario;
import ru.snptech.businessbanyabot.service.scenario.event.EventScenario;
import ru.snptech.businessbanyabot.service.scenario.search.SearchScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Slider.*;
import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.User.USER_CHOOSE_EVENT_TYPE;
import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class UserCallbackScenario extends BaseCallbackScenario {

    private final PaymentScenario paymentScenario;
    private final SearchScenario searchScenario;
    private final EventScenario eventScenario;
    private final UserContextService userContextService;
    private final UserRepository userRepository;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var chatId = CHAT_ID.getValue(requestContext, Long.class);

        var callback = TG_UPDATE.getValue(requestContext).getCallbackQuery();
        var callbackPrefix = extractCallbackPrefix(callback.getData());
        var callbackPostfix = extractCallbackPostfix(callback.getData());

        switch (callbackPrefix) {
            case CallbackPrefixes.User.USER_CHOOSE_FAST_PAYMENT ->
                paymentScenario.handlePayment(PaymentType.FAST_PAYMENT, requestContext);

            case CallbackPrefixes.User.USER_CHOOSE_INVOICE_PAYMENT ->
                paymentScenario.handlePayment(PaymentType.INVOICE, requestContext);

            case CallbackPrefixes.User.USER_DEPOSIT_FAST_PAYMENT ->
                paymentScenario.handlePayment(PaymentType.DEPOSIT_FAST_PAYMENT, requestContext);

            case CallbackPrefixes.User.USER_DEPOSIT_INVOICE_PAYMENT ->
                paymentScenario.handlePayment(PaymentType.DEPOSIT_INVOICE, requestContext);

            case RESIDENT_SLIDER_NEXT_CARD_PREFIX -> searchScenario.slideTo(SlideDirection.NEXT, requestContext);
            case RESIDENT_SLIDER_PREVIOUS_CARD_PREFIX ->
                searchScenario.slideTo(SlideDirection.PREVIOUS, requestContext);

            case USER_CHOOSE_EVENT_TYPE -> {
                var user = userRepository.findByChatId(chatId);

                SCENARIO_STEP.setValue(requestContext, EventScenarioStep.EVENT_SLIDER.name());
                EVENT_TYPE.setValue(requestContext, callbackPostfix);

                userContextService.updateUserContext(user, requestContext);

                eventScenario.invoke(requestContext);
            }

            case EVENT_SLIDER_NEXT_CARD_PREFIX -> eventScenario.slideTo(SlideDirection.NEXT, requestContext);
            case EVENT_SLIDER_PREVIOUS_CARD_PREFIX -> eventScenario.slideTo(SlideDirection.PREVIOUS, requestContext);
        }

        releaseCallback(requestContext);
    }

    public UserCallbackScenario(
        TelegramClientAdapter telegramClientAdapter,
        PaymentScenario paymentScenario,
        SearchScenario searchScenario,
        EventScenario eventScenario,
        UserContextService userContextService,
        UserRepository userRepository
    ) {
        super(telegramClientAdapter);

        this.eventScenario = eventScenario;
        this.searchScenario = searchScenario;
        this.paymentScenario = paymentScenario;
        this.userContextService = userContextService;
        this.userRepository = userRepository;
    }
}
