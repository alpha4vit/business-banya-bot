package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.model.common.CallbackPrefixes;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.service.scenario.BaseCallbackScenario;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.TG_UPDATE;

@Component
public class UserCallbackScenario extends BaseCallbackScenario {

    private final PaymentScenario paymentScenario;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var callback = TG_UPDATE.getValue(requestContext).getCallbackQuery();
        var callbackPrefix = extractCallbackPrefix(callback.getData());

        switch (callbackPrefix) {
            case CallbackPrefixes.User.USER_CHOOSE_FAST_PAYMENT -> {
                paymentScenario.handle(PaymentType.FAST_PAYMENT, requestContext);
            }

            case CallbackPrefixes.User.USER_CHOOSE_INVOICE_PAYMENT -> {
                paymentScenario.handle(PaymentType.INVOICE, requestContext);
            }
        }

        releaseCallback(requestContext);
    }

    public UserCallbackScenario(TelegramClientAdapter telegramClientAdapter, PaymentScenario paymentScenario) {
        super(telegramClientAdapter);
        this.paymentScenario = paymentScenario;
    }
}
