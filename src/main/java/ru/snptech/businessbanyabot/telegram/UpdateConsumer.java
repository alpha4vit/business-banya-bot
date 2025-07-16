package ru.snptech.businessbanyabot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.snptech.businessbanyabot.telegram.scenario.RegistrationScenario;
import ru.snptech.businessbanyabot.telegram.scenario.VerificationScenario;
import ru.snptech.businessbanyabot.telegram.scenario.admin.AdminUpdateScenario;
import ru.snptech.businessbanyabot.telegram.scenario.user.UserUpdateScenario;

import java.util.HashMap;
import java.util.Map;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.IS_ADMIN;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final RegistrationScenario registrationScenario;
    private final AdminUpdateScenario adminUpdateScenario;
    private final VerificationScenario verificationScenario;
    private final UserUpdateScenario userUpdateScenario;

    @Override
    public void consume(Update update) {
        Map<String, Object> requestContext = new HashMap<>();
        TG_UPDATE.setValue(requestContext, update);

        registrationScenario.invoke(requestContext);

        if (IS_ADMIN.getValue(requestContext)) {
            adminUpdateScenario.invoke(requestContext);
        } else {
            var isVerified = verificationScenario.verifyIfNeeded(requestContext);

            if (isVerified) {
                userUpdateScenario.invoke(requestContext);
            }
        }
    }
}
