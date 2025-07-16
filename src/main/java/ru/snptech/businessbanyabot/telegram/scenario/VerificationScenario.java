package ru.snptech.businessbanyabot.telegram.scenario;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.MessageConstants;
import ru.snptech.businessbanyabot.telegram.scenario.user.UserMainMenuScenario;

import java.util.Map;

import static ru.snptech.businessbanyabot.telegram.TelegramUtils.extractChatIdFromUpdate;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.*;

@Component
@RequiredArgsConstructor
public class VerificationScenario extends AbstractScenario {

    private final TelegramClient telegramClient;
    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final UserMainMenuScenario userMainMenuScenario;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var update = TG_UPDATE.getValue(requestContext);
        var chatId = extractChatIdFromUpdate(update);

        var user = userRepository.findByChatId(chatId);

        var context = userContextService.getUserContext(user);

        if (IS_ADMIN.getValue(context)) return;

        if (IS_VERIFIED.getValue(context) != null) {
            userMainMenuScenario.invoke(context);
        }

        telegramClient.execute(createSendMessage(String.valueOf(chatId), MessageConstants.VERIFICATION_NEED_MESSAGE));

        IS_VERIFIED.setValue(context, false);
        userContextService.updateUserContext(user, context);
    }

    @SneakyThrows
    public Boolean verifyIfNeeded(Map<String, Object> requestContext) {
        var update = TG_UPDATE.getValue(requestContext);
        var chatId = extractChatIdFromUpdate(update);

        var user = userRepository.findByChatId(chatId);

        var context = userContextService.getUserContext(user);

        if (IS_VERIFIED.getValue(context)) return true;

        telegramClient.execute(createSendMessage(String.valueOf(chatId), MessageConstants.VERIFICATION_NEED_MESSAGE));

        IS_VERIFIED.setValue(context, false);
        userContextService.updateUserContext(user, context);

        return false;
    }
}
