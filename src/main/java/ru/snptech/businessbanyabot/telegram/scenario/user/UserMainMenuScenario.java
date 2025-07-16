package ru.snptech.businessbanyabot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.MenuConstants;
import ru.snptech.businessbanyabot.telegram.MessageConstants;
import ru.snptech.businessbanyabot.telegram.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.telegram.service.TelegramDealSenderService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;

@Component
@RequiredArgsConstructor
public class UserMainMenuScenario extends AbstractScenario {
    private final TelegramClient telegramClient;
    private final TelegramDealSenderService telegramDealSenderService;

    public static final String REPORTS_COMMAND = "\uD83D\uDDC3 Отчеты";
    public static final String PARTNERS = "Управление ассистентами";
    public static final String PARTNERS_ADD = "Добавить ассистента";
    public static final String PARTNERS_REMOVE = "Удалить ассистента";
    public static final String MAIN_MENU = "Главное меню";
    public static final String STATISTICS_COMMAND = "\uD83D\uDCCA Статистика";
    public static final String ACTIVE_COMMAND = "\uD83D\uDCCA Активные заявки";
    public static final String CLOSED_COMMAND = "\uD83D\uDCCA Закрытые заявки";
    public static final Set<String> MAIN_MENU_COMMANDS = Set.of(
            REPORTS_COMMAND, PARTNERS, PARTNERS_ADD, PARTNERS_REMOVE, MAIN_MENU, STATISTICS_COMMAND, ACTIVE_COMMAND, CLOSED_COMMAND
    );
    private final AddPartnerScenario addPartnerScenario;
    private final UserContextService userContextService;
    private final UserReportGenerationScenario userReportGenerationScenario;
    private final UserRepository userRepository;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        if (tgUpdate.getMessage().hasText()) {
            switch (tgUpdate.getMessage().getText()) {
                case REPORTS_COMMAND -> {
                    userReportGenerationScenario.invoke(AUTHENTICATED_USER.getValue(requestContext));
                }
                case STATISTICS_COMMAND -> {
                    telegramDealSenderService.sendMyStatistic(AUTHENTICATED_USER.getValue(requestContext));
                }
                case ACTIVE_COMMAND -> {
                    telegramDealSenderService.sendMyActive(AUTHENTICATED_USER.getValue(requestContext));
                }
                case CLOSED_COMMAND -> {
                    telegramDealSenderService.sendMyClosed(AUTHENTICATED_USER.getValue(requestContext));
                }
                case PARTNERS -> {
                    telegramClient.execute(
                            SendMessage.builder()
                                    .chatId(CHAT_ID.getValue(requestContext))
                                    .text("Управление ассистентами")
                                    .replyMarkup(
                                            ReplyKeyboardMarkup.builder()
                                                    .resizeKeyboard(true)
                                                    .keyboard(List.of(
                                                            new KeyboardRow(new KeyboardButton(PARTNERS_ADD)),
                                                            new KeyboardRow(new KeyboardButton(PARTNERS_REMOVE)),
                                                            new KeyboardRow(new KeyboardButton(MAIN_MENU))
                                                    ))
                                                    .build()
                                    )
                                    .build()
                    );
                }
                case PARTNERS_ADD -> {
                    if (AUTHENTICATED_USER.getValue(requestContext).getPartnerAccount() == null) {
                        userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
                        addPartnerScenario.invoke(requestContext);
                    }
                }
                case PARTNERS_REMOVE -> {
                    if (AUTHENTICATED_USER.getValue(requestContext).getPartnerAccount() == null) {
                        userContextService.cleanUserContext(AUTHENTICATED_USER.getValue(requestContext));
                        var partners = userRepository.findAllByPartnerAccount_ChatId(AUTHENTICATED_USER.getValue(requestContext).getChatId());
                        if (partners == null || partners.isEmpty()) {
                            telegramClient.execute(
                                    SendMessage.builder()
                                            .chatId(CHAT_ID.getValue(requestContext))
                                            .text("У вас нет ассистентов!")
                                            .build()
                            );
                        } else {
                            telegramClient.execute(
                                    SendMessage.builder()
                                            .chatId(CHAT_ID.getValue(requestContext))
                                            .text("Выберите ассистента для удаления")
                                            .replyMarkup(
                                                    InlineKeyboardMarkup.builder()
                                                            .keyboard(
                                                                    partners.stream().map(partner -> {
                                                                        return new InlineKeyboardRow(List.of(
                                                                                InlineKeyboardButton.builder()
                                                                                        .text(partner.getTelegramUsername())
                                                                                        .callbackData(UserCallbackScenario.REMOVE_PARTNER_PREFIX + partner.getChatId())
                                                                                        .build()
                                                                        ));
                                                                    }).toList()
                                                            )
                                                            .build()
                                            )
                                            .build()
                            );
                        }
                    }
                }
                default -> {
                    telegramClient.execute(createSendMessage(
                            CHAT_ID.getValue(requestContext),
                            MessageConstants.MAIN_MENU,
                            MenuConstants.createUserMainMenu(
                                    AUTHENTICATED_USER.getValue(requestContext).getPartnerAccount() == null
                            )
                    ));
                }
            }
        }
    }

}
