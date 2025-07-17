package ru.snptech.businessbanyabot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.repository.impl.JdbcUserRepository;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.MenuConstants;
import ru.snptech.businessbanyabot.telegram.MessageConstants;
import ru.snptech.businessbanyabot.telegram.scenario.AbstractScenario;

import java.util.Map;
import java.util.Set;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;

@Component
@RequiredArgsConstructor
public class UserMainMenuScenario extends AbstractScenario {
    private final TelegramClient telegramClient;

    public static final String SEARCH = "Поиск";
    public static final String BALANCE = "Мой баланс";
    public static final String EVENTS = "События";
    public static final String MAIN_MENU = "Главное меню";
    public static final Set<String> MAIN_MENU_COMMANDS = Set.of(
        SEARCH, BALANCE, EVENTS
    );
    private final UserContextService userContextService;
    private final JdbcUserRepository userRepository;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.getMessage().hasText()) {
            switch (tgUpdate.getMessage().getText()) {
                default -> {
                    telegramClient.execute(
                        createSendMessage(
                            CHAT_ID.getValue(requestContext),
                            MessageConstants.MAIN_MENU,
                            MenuConstants.createUserMainMenu()
                        ));
                }
            }
        }
    }

}
