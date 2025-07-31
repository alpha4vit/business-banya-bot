package ru.snptech.businessbanyabot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.service.scenario.VerificationScenario;
import ru.snptech.businessbanyabot.service.scenario.admin.AdminUpdateScenario;
import ru.snptech.businessbanyabot.service.scenario.user.RegistrationScenario;
import ru.snptech.businessbanyabot.service.scenario.user.UserUpdateScenario;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.HashMap;
import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.IS_VERIFIED;
import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.TG_UPDATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final RegistrationScenario registrationScenario;
    private final VerificationScenario verificationScenario;
    private final UserUpdateScenario userUpdateScenario;
    private final AdminUpdateScenario adminUpdateScenario;

    private final TelegramClientAdapter telegramClientAdapter;

    @Override
    public void consume(Update update) {
        var chatId = TelegramUtils.extractChatIdFromUpdate(update);

        try {
            Map<String, Object> requestContext = new HashMap<>();
            TG_UPDATE.setValue(requestContext, update);

            var user = registrationScenario.invoke(requestContext);

            if (UserRole.ADMIN.equals(user.getRole())) {
                adminUpdateScenario.invoke(requestContext);

                return;
            }

            verificationScenario.invoke(requestContext);

            if (Boolean.TRUE.equals(IS_VERIFIED.getValue(requestContext))) {
                userUpdateScenario.invoke(requestContext);
            }
        } catch (BusinessBanyaInternalException e) {
            telegramClientAdapter.sendMessage(chatId, e.getMessage());

            throw e;
        } catch (BusinessBanyaDomainLogicException e) {
            telegramClientAdapter.sendMessage(chatId, e.getMessage());

            throw e;
        } catch (Throwable t) {
            telegramClientAdapter.sendMessage(chatId, t.getMessage());

            throw t;
        }

    }
}
