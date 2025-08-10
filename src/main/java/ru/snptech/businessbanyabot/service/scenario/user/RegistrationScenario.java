package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixCompanyDto;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.model.user.UserStatus;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.TelegramUtils;

import java.util.Map;
import java.util.stream.Collectors;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
@RequiredArgsConstructor
public class RegistrationScenario {
    private final UserRepository userRepository;
    private final UserContextService userContextService;

    public TelegramUser invoke(Map<String, Object> requestContext) {
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

            return user;
        }

        var tgUser = TelegramUtils.extractUserFromUpdate(tgUpdate);
        user = new TelegramUser();
        user.setChatId(chatId);
        user.setTelegramFirstName(tgUser.getFirstName());
        user.setTelegramLastName(tgUser.getLastName());
        user.setTelegramUsername(tgUser.getUserName());
        user.setRole(UserRole.NON_RESIDENT);
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);

        CHAT_ID.setValue(requestContext, user.getChatId().toString());
        SCENARIO.setValue(requestContext, ScenarioType.VERIFICATION.name());

        userContextService.updateUserContext(user, requestContext);

        return user;
    }
}
