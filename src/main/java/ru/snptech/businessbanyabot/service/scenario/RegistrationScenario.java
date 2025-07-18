package ru.snptech.businessbanyabot.service.scenario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.Role;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.TelegramUtils;

import java.util.Map;
import java.util.stream.Collectors;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.*;

@Component
@RequiredArgsConstructor
public class RegistrationScenario {
    private final UserRepository userRepository;
    private final UserContextService userContextService;

    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        var chatId = TelegramUtils.extractChatIdFromUpdate(tgUpdate);
        var user = userRepository.findByChatId(chatId);

        if (user != null) {
            var context = userContextService.getUserContext(user);

            Map<String, Object> contextWithoutUpdates = context.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(TG_UPDATE.toString()))
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue
                ));

            requestContext.putAll(contextWithoutUpdates);
            AUTHENTICATED_USER.setValue(requestContext, user);
            return;
        }

        var tgUser = TelegramUtils.extractUserFromUpdate(tgUpdate);
        user = new TelegramUser();
        user.setChatId(chatId);
        user.setTelegramFirstName(tgUser.getFirstName());
        user.setTelegramLastName(tgUser.getLastName());
        user.setTelegramUsername(tgUser.getUserName());
        user.setRole(Role.NON_RESIDENT);
        user = userRepository.save(user);

        AUTHENTICATED_USER.setValue(requestContext, user);
        CHAT_ID.setValue(requestContext, user.getChatId().toString());
        SCENARIO.setValue(requestContext, ScenarioType.VERIFICATION.name());
        USER_ROLE.setValue(requestContext, user.getRole());

        userContextService.updateUserContext(user, requestContext);
    }
}
