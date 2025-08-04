package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.model.common.CallbackPrefixes;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.model.search.SlideDirection;
import ru.snptech.businessbanyabot.service.scenario.BaseCallbackScenario;
import ru.snptech.businessbanyabot.service.scenario.search.SearchScenario;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Slider.SLIDER_NEXT_CARD_PREFIX;
import static ru.snptech.businessbanyabot.model.common.CallbackPrefixes.Slider.SLIDER_PREVIOUS_CARD_PREFIX;
import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.TG_UPDATE;

@Component
public class UserCallbackScenario extends BaseCallbackScenario {

    private final PaymentScenario paymentScenario;
    private final SearchScenario searchScenario;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var callback = TG_UPDATE.getValue(requestContext).getCallbackQuery();
        var callbackPrefix = extractCallbackPrefix(callback.getData());

        switch (callbackPrefix) {
            case CallbackPrefixes.User.USER_CHOOSE_FAST_PAYMENT ->
                paymentScenario.handlePayment(PaymentType.FAST_PAYMENT, requestContext);

            case CallbackPrefixes.User.USER_CHOOSE_INVOICE_PAYMENT ->
                paymentScenario.handlePayment(PaymentType.INVOICE, requestContext);

            case CallbackPrefixes.User.USER_DEPOSIT_FAST_PAYMENT ->
                paymentScenario.handlePayment(PaymentType.DEPOSIT_FAST_PAYMENT, requestContext);

            case CallbackPrefixes.User.USER_DEPOSIT_INVOICE_PAYMENT ->
                paymentScenario.handlePayment(PaymentType.DEPOSIT_INVOICE, requestContext);

            case SLIDER_NEXT_CARD_PREFIX -> searchScenario.slideTo(SlideDirection.NEXT, requestContext);

            case SLIDER_PREVIOUS_CARD_PREFIX -> searchScenario.slideTo(SlideDirection.PREVIOUS, requestContext);
        }

        releaseCallback(requestContext);
    }

    public UserCallbackScenario(
        TelegramClientAdapter telegramClientAdapter,
        PaymentScenario paymentScenario,
        SearchScenario searchScenario
    ) {
        super(telegramClientAdapter);

        this.searchScenario = searchScenario;
        this.paymentScenario = paymentScenario;
    }
}
