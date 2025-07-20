package ru.snptech.businessbanyabot.service.scenario.common;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.service.scenario.user.UserState;
import ru.snptech.businessbanyabot.model.common.MessageConstants;

import java.util.List;
import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
@RequiredArgsConstructor
public class UserCallbackScenario extends AbstractScenario {
    private final TelegramClient telegramClient;
    private final UserContextService userContextService;

    public static final String USER_DEAL_APPROVE_CALLBACK_PREFIX = "UDA_";
    public static final String USER_DEAL_REJECT_CALLBACK_PREFIX = "UDR_";
    public static final String USER_DEAL_SECOND_APPROVE_CALLBACK_PREFIX = "UDSA_";
    public static final String USER_DEAL_SECOND_REJECT_CALLBACK_PREFIX = "UDSR_";
    public static final String USER_DEAL_SECOND_PHONE_CALLBACK_PREFIX = "UDSP_";

    public static final String USER_DEAL_COMMISSION_ENTER = "UDCOMME_"; // Инициировать ввод комиссии заказа
    public static final String USER_DEAL_AMOUNT_ENTER = "UDAE_"; // Инициировать ввод суммы заказа

    public static final String SHOW_DEAL_PREFIX = "SD_"; // Показать сделки из меню

    public static final String REMOVE_PARTNER_PREFIX = "RP_"; // Показать сделки из меню
    public static final String REMOVE_PARTNER_CONFIRM_PREFIX = "RPC_"; // Показать сделки из меню
    public static final String REMOVE_MESSAGE = "RM_"; // Показать сделки из меню

    public static final String ADMIN_SURVEY_ACCEPT_PREFIX = "ASA_";
    public static final String ADMIN_SURVEY_DECLINE_PREFIX = "ASD_";


    private final UserRepository userRepository;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var callback = TG_UPDATE.getValue(requestContext).getCallbackQuery();
        var callbackPrefix = extractCallbackPrefix(callback.getData());

        switch (callbackPrefix) {
            case ADMIN_SURVEY_DECLINE_PREFIX -> {}
        }
        releaseCallback(requestContext);
    }

    @SneakyThrows
    private void releaseCallback(Map<String, Object> requestContext) {
        telegramClient.execute(new AnswerCallbackQuery(TG_UPDATE.getValue(requestContext).getCallbackQuery().getId()));
    }

    @SneakyThrows
    private void sendMessage(Map<String, Object> requestContext, String message) {
        telegramClient.execute(createSendMessage(
                CHAT_ID.getValue(requestContext),
                message,
                (String) null
        ));
    }

    @SneakyThrows
    private Message sendMessage(Map<String, Object> requestContext, String message, ReplyKeyboard replyKeyboard) {
        return telegramClient.execute(createSendMessage(
                CHAT_ID.getValue(requestContext),
                message,
                replyKeyboard
        ));
    }

    private static String extractCallbackPrefix(String callback) {
        return callback.split("_")[0] + "_";
    }

    private static String extractCallbackPostfix(String callback) {
        return callback.split("_")[1];
    }

}
