package ru.snptech.businessbanyabot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.snptech.businessbanyabot.service.scenario.RegistrationScenario;
import ru.snptech.businessbanyabot.service.scenario.VerificationScenario;
import ru.snptech.businessbanyabot.service.scenario.common.UserUpdateScenario;

import java.util.HashMap;
import java.util.Map;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.IS_VERIFIED;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final RegistrationScenario registrationScenario;
    private final VerificationScenario verificationScenario;
    private final UserUpdateScenario userUpdateScenario;

    @Override
    public void consume(Update update) {
        Map<String, Object> requestContext = new HashMap<>();
        TG_UPDATE.setValue(requestContext, update);

        registrationScenario.invoke(requestContext);

        verificationScenario.invoke(requestContext);

        if (Boolean.TRUE.equals(IS_VERIFIED.getValue(requestContext))) {
            userUpdateScenario.invoke(requestContext);
        }
    }
}
