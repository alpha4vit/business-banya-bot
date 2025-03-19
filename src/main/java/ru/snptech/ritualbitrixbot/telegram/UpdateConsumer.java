package ru.snptech.ritualbitrixbot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.snptech.ritualbitrixbot.telegram.scenario.AuthenticationScenario;
import ru.snptech.ritualbitrixbot.telegram.scenario.CheckUsernameScenario;
import ru.snptech.ritualbitrixbot.telegram.scenario.admin.AdminUpdateScenario;
import ru.snptech.ritualbitrixbot.telegram.scenario.user.UserRegistrationScenario;
import ru.snptech.ritualbitrixbot.telegram.scenario.user.UserUpdateScenario;

import java.util.HashMap;
import java.util.Map;

import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.HAS_USERNAME;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.IS_ADMIN;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final AuthenticationScenario authenticationScenario;
    private final UserRegistrationScenario userRegistrationScenario;
    private final AdminUpdateScenario adminUpdateScenario;
    private final CheckUsernameScenario checkUsernameScenario;
    private final UserUpdateScenario userUpdateScenario;

    @Override
    public void consume(Update update) {
        Map<String, Object> requestContext = new HashMap<>();
        TG_UPDATE.setValue(requestContext, update);
        checkUsernameScenario.invoke(requestContext);
        if (!HAS_USERNAME.getValue(requestContext)) {
            return;
        }
        authenticationScenario.invoke(requestContext);
        if (IS_ADMIN.getValue(requestContext)) {
            adminUpdateScenario.invoke(requestContext);
        } else {
            if (userRegistrationScenario.isNeedInvoke(requestContext)) {
                userRegistrationScenario.invoke(requestContext);
                return;
            }
            if (!AUTHENTICATED_USER.getValue(requestContext).isActive() && AUTHENTICATED_USER.getValue(requestContext).isRegistered()) {
                log.info("Запрос пользователя {} заблокирован!", CHAT_ID.getValue(requestContext));
                return;
            }
            userUpdateScenario.invoke(requestContext);
        }
    }
}
