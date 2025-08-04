package ru.snptech.businessbanyabot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.integration.bitrix.client.BitrixCrmClient;
import ru.snptech.businessbanyabot.integration.bitrix.dto.filter.BitrixListFilter;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.service.scenario.VerificationScenario;
import ru.snptech.businessbanyabot.service.scenario.admin.AdminUpdateScenario;
import ru.snptech.businessbanyabot.service.scenario.user.RegistrationScenario;
import ru.snptech.businessbanyabot.service.scenario.user.UserUpdateScenario;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.HashMap;
import java.util.List;
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

    private final BitrixCrmClient bitrixCrmClient;

    @Override
    public void consume(Update update) {
        var chatId = TelegramUtils.extractChatIdFromUpdate(update);

        try {
            Map<String, Object> requestContext = new HashMap<>();
            TG_UPDATE.setValue(requestContext, update);

            var filter = new BitrixListFilter(
                Map.of("CATEGORY_ID", "58"),
                List.of(
                    "TITLE",
                    "UF_CRM_1754335478118",
                    "UF_CRM_1743440819598",
                    "UF_CRM_1754335207065",
                    "UF_CRM_1754335220238",
                    "UF_CRM_1745990408213",
                    "UF_CRM_1745990553362",
                    "UF_CRM_1754335612157"
                ),
                0
            );
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
