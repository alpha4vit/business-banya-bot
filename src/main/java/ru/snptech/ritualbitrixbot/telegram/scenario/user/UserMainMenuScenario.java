package ru.snptech.ritualbitrixbot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.telegram.MenuConstants;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;
import ru.snptech.ritualbitrixbot.telegram.scenario.AbstractScenario;

import java.util.Map;

import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;

@Component
@RequiredArgsConstructor
public class UserMainMenuScenario extends AbstractScenario {
    private final TelegramClient telegramClient;

    public static final String REPORTS_COMMAND = "\uD83D\uDDC3 Отчеты";
    public static final String STATISTICS_COMMAND = "\uD83D\uDCCA Статистика";

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.getMessage().hasText()) {
            switch (tgUpdate.getMessage().getText()) {
                case REPORTS_COMMAND -> {

                }
                case STATISTICS_COMMAND -> {

                }
                default -> {
                    telegramClient.execute(createSendMessage(
                            CHAT_ID.getValue(requestContext),
                            MessageConstants.MAIN_MENU,
                            MenuConstants.createUserMainMenu()
                    ));
                }
            }
        }
    }

}
