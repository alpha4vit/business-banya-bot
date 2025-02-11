package ru.snptech.ritualbitrixbot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.entity.Deal;
import ru.snptech.ritualbitrixbot.entity.DealFlow;
import ru.snptech.ritualbitrixbot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.ritualbitrixbot.model.RejectReason;
import ru.snptech.ritualbitrixbot.repository.DealRepository;
import ru.snptech.ritualbitrixbot.service.DealService;
import ru.snptech.ritualbitrixbot.service.UserContextService;
import ru.snptech.ritualbitrixbot.telegram.MenuConstants;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;
import ru.snptech.ritualbitrixbot.telegram.scenario.AbstractScenario;

import java.util.Arrays;
import java.util.Map;

import static ru.snptech.ritualbitrixbot.telegram.TelegramUtils.escapeMarkdownV2;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.CURRENT_DEAL_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.TG_UPDATE;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.USER_STATE;

@Component
@RequiredArgsConstructor
public class UserCallbackScenario extends AbstractScenario {
    private final TelegramClient telegramClient;
    private final UserContextService userContextService;
    private final DealRepository dealRepository;
    private final BitrixIntegrationService bitrixIntegrationService;

    public static final String USER_DEAL_APPROVE_CALLBACK_PREFIX = "UDA_";
    public static final String USER_DEAL_REJECT_CALLBACK_PREFIX = "UDR_";
    public static final String USER_DEAL_SECOND_APPROVE_CALLBACK_PREFIX = "UDSA_";
    public static final String USER_DEAL_SECOND_REJECT_CALLBACK_PREFIX = "UDSR_";
    public static final String USER_DEAL_SECOND_PHONE_CALLBACK_PREFIX = "UDSP_";
    public static final String USER_DEAL_REJECT_CUSTOMER_CHANGED_MIND = "UDRCCM_"; // Заказчик передумал
    public static final String USER_DEAL_REJECT_ANOTHER_AGENT_ON_ADDRESS = "UDRAAOA_"; // На адресе был другой агент
    public static final String USER_DEAL_REJECT_NO_CALL_ANSWER = "UDRNCA_"; // Не отвечают на звонки
    public static final String USER_DEAL_REJECT_NO_TOUCH = "UDRNT_"; // Не удалось связаться
    public static final String USER_DEAL_REJECT_BAD_PRICE = "UDRBP_"; // Не устроила цена
    public static final String USER_DEAL_REJECT_NO_MONEY = "UDRNM_"; // Нет денег
    public static final String USER_DEAL_REJECT_REFUSED_WHILE_TRANSFER = "UDRRWT_"; // Отказались пока ехал
    public static final String USER_DEAL_REJECT_ANOTHER_COMPANY = "UDRAC_"; // Оформили сами в другой компании
    public static final String USER_DEAL_REJECT_FRIENDS_HELP = "UDRFH_"; // Оформили через знакомых
    public static final String USER_DEAL_REJECT_ANOTHER_AGENT_INTERRUPTED = "UDRAAI_"; // Перебил другой агент
    public static final String USER_DEAL_REJECT_MORGUE_INTERRUPTED = "UDRMI_"; // Перебили в морге
    public static final String USER_DEAL_REJECT_DONT_LET_GO = "UDRDLG_"; // Приехал. Не пустили
    public static final String USER_DEAL_REJECT_OWN_AGENT = "UDROA_"; // Есть свой агент
    public static final String USER_DEAL_REJECT_THIRD_PERSON = "UDRTP_"; // 3-е лицо
    public static final String USER_DEAL_REJECT_CONSULTATION = "UDRC_"; // Консультация
    public static final String USER_DEAL_REJECT_BAD_DELIVERY_PRICE = "UDRBDP_"; // Не устроила сумма доставки
    public static final String USER_DEAL_REJECT_BAD_DELIVERY_TIME = "UDRBDT_"; // Не устроило время доставки
    private final DealService dealService;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var authUser = AUTHENTICATED_USER.getValue(requestContext);
        var callback = TG_UPDATE.getValue(requestContext).getCallbackQuery();
        var callbackPrefix = extractCallbackPrefix(callback.getData());
        var deal = dealRepository.findDealById(extractCallbackDealId(callback.getData()));
        switch (callbackPrefix) {
            case USER_DEAL_APPROVE_CALLBACK_PREFIX -> {
                if (deal.getFlow() == DealFlow.SHOP) {
                    bitrixIntegrationService.inWorkShop(deal.getId());
                    var text = MessageConstants.SHOP_DEAL_MESSAGE_TEMPLATE.formatted(
                            escapeMarkdownV2(deal.getId()),
                            escapeMarkdownV2(deal.getDealType()),
                            escapeMarkdownV2(deal.getSource()),
                            escapeMarkdownV2(deal.getSource2()),
                            escapeMarkdownV2(deal.getComment()),
                            escapeMarkdownV2(deal.getContactName()),
                            escapeMarkdownV2(deal.getContactPhone())
                    );
                    sendMessage(requestContext, text, MenuConstants.dealSecondMenu(deal.getId()));
                } else if (deal.getFlow() == DealFlow.FUNERAL) {
                    bitrixIntegrationService.inWorkFuneral(deal.getId());
                    var text = MessageConstants.FUNERAL_DEAL_MESSAGE_TEMPLATE.formatted(
                            escapeMarkdownV2(deal.getId()),
                            escapeMarkdownV2(deal.getDealType()),
                            escapeMarkdownV2(deal.getSource()),
                            escapeMarkdownV2(deal.getSource2()),
                            escapeMarkdownV2(deal.getComment()),
                            escapeMarkdownV2(deal.getAddress()),
                            escapeMarkdownV2(deal.getCity()),
                            escapeMarkdownV2(deal.getCustomerName()),
                            escapeMarkdownV2(deal.getDeceasedSurname())
                    );
                    sendMessage(requestContext, text, MenuConstants.funeralMenu(deal.getId()));
                }
            }
            case USER_DEAL_SECOND_REJECT_CALLBACK_PREFIX -> {
                sendMessage(requestContext, MessageConstants.REJECT_INPUT_MESSAGE, MenuConstants.createRejectReasonsMenu(deal.getId()));
            }
            case USER_DEAL_REJECT_CALLBACK_PREFIX -> {
                updateUserState(requestContext, UserState.WAITING_PROBLEM);
                userContextService.updateUserContext(authUser, CURRENT_DEAL_ID, deal.getId());
                sendMessage(requestContext, MessageConstants.PROBLEM_INPUT_MESSAGE);
            }
            case USER_DEAL_SECOND_APPROVE_CALLBACK_PREFIX -> {
                updateUserState(requestContext, UserState.WAITING_AMOUNT);
                userContextService.updateUserContext(authUser, CURRENT_DEAL_ID, deal.getId());
                sendMessage(requestContext, MessageConstants.WAITING_AMOUNT_INPUT_MESSAGE);
            }
            case USER_DEAL_SECOND_PHONE_CALLBACK_PREFIX -> {
                sendMessage(requestContext, MessageConstants.PHONE_REQUESTED_MESSAGE);
                bitrixIntegrationService.needPhone(deal.getId());
            }
            default -> {
                var rejectReason = Arrays.stream(RejectReason.values())
                        .filter(reason -> reason.getCallbackPrefix().equals(callbackPrefix))
                        .findFirst();
                if (rejectReason.isPresent()) {
                    bitrixIntegrationService.reject(deal.getId(), rejectReason.get().getBitrixValue());
                    dealService.finishDeal(deal.getId(), false);
                    telegramClient.execute(sentToBitrix(CHAT_ID.getValue(requestContext)));
                }
            }
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
    private void sendMessage(Map<String, Object> requestContext, String message, ReplyKeyboard replyKeyboard) {
        telegramClient.execute(createSendMessage(
                CHAT_ID.getValue(requestContext),
                message,
                replyKeyboard
        ));
    }

    private void updateUserState(Map<String, Object> requestContext, UserState userState) {
        userContextService.updateUserContext(AUTHENTICATED_USER.getValue(requestContext), USER_STATE, userState.name());
    }

    private static String extractCallbackPrefix(String callback) {
        return callback.split("_")[0] + "_";
    }

    private static String extractCallbackDealId(String callback) {
        return callback.split("_")[1];
    }

}
