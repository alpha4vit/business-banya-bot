package ru.snptech.businessbanyabot.telegram.scenario.user;

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
import ru.snptech.businessbanyabot.entity.DealActionStatus;
import ru.snptech.businessbanyabot.entity.DealFlow;
import ru.snptech.businessbanyabot.entity.LinkedMessageEntity;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.businessbanyabot.model.RejectReason;
import ru.snptech.businessbanyabot.repository.DealRepository;
import ru.snptech.businessbanyabot.repository.LinkedMessagesRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.DealService;
import ru.snptech.businessbanyabot.service.UserContextService;
import ru.snptech.businessbanyabot.telegram.MenuConstants;
import ru.snptech.businessbanyabot.telegram.MessageConstants;
import ru.snptech.businessbanyabot.telegram.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.telegram.service.TelegramDealSenderService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.AUTHENTICATED_USER;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.CURRENT_DEAL_ID;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.TG_UPDATE;
import static ru.snptech.businessbanyabot.types.ServiceConstantHolder.USER_STATE;

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

    public static final String USER_DEAL_COMMISSION_ENTER = "UDCOMME_"; // Инициировать ввод комиссии заказа
    public static final String USER_DEAL_AMOUNT_ENTER = "UDAE_"; // Инициировать ввод суммы заказа

    public static final String SHOW_DEAL_PREFIX = "SD_"; // Показать сделки из меню

    public static final String REMOVE_PARTNER_PREFIX = "RP_"; // Показать сделки из меню
    public static final String REMOVE_PARTNER_CONFIRM_PREFIX = "RPC_"; // Показать сделки из меню
    public static final String REMOVE_MESSAGE = "RM_"; // Показать сделки из меню
    private final DealService dealService;
    private final TelegramDealSenderService telegramDealSenderService;
    private final UserRepository userRepository;
    private final LinkedMessagesRepository linkedMessagesRepository;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var authUser = AUTHENTICATED_USER.getValue(requestContext);
        var callback = TG_UPDATE.getValue(requestContext).getCallbackQuery();
        var callbackPrefix = extractCallbackPrefix(callback.getData());
        var deal = dealRepository.findDealById(extractCallbackPostfix(callback.getData()));
        switch (callbackPrefix) {
            case REMOVE_MESSAGE -> {
                telegramClient.execute(
                        DeleteMessage.builder()
                                .chatId(CHAT_ID.getValue(requestContext))
                                .messageId(callback.getMessage().getMessageId())
                                .build()
                );
            }
            case REMOVE_PARTNER_CONFIRM_PREFIX -> {
                var user = userRepository.findByChatId(Long.valueOf(extractCallbackPostfix(callback.getData())));
                user.setPartnerAccount(null);
                userRepository.save(user);
                telegramClient.execute(
                    EditMessageText.builder()
                            .chatId(CHAT_ID.getValue(requestContext))
                            .messageId(callback.getMessage().getMessageId())
                            .text("Ассистент " + user.getTelegramUsername() + " успешно удален!")
                            .build()
                );
            }
            case REMOVE_PARTNER_PREFIX -> {
                var user = userRepository.findByChatId(Long.valueOf(extractCallbackPostfix(callback.getData())));
                telegramClient.execute(
                        EditMessageText.builder()
                                .chatId(CHAT_ID.getValue(requestContext))
                                .messageId(callback.getMessage().getMessageId())
                                .text("Вы действительно хотите удалить ассистента " + user.getTelegramUsername() + " ?")
                                .build()
                );
                telegramClient.execute(
                        EditMessageReplyMarkup.builder()
                                .chatId(CHAT_ID.getValue(requestContext))
                                .messageId(callback.getMessage().getMessageId())
                                .replyMarkup(
                                        InlineKeyboardMarkup.builder()
                                                .keyboardRow(new InlineKeyboardRow(List.of(
                                                        InlineKeyboardButton.builder().text("Да").callbackData(REMOVE_PARTNER_CONFIRM_PREFIX + user.getChatId()).build(),
                                                        InlineKeyboardButton.builder().text("Нет").callbackData(REMOVE_MESSAGE + user.getChatId()).build()
                                                )))
                                                .build()
                                )
                                .build()
                );
            }
            case SHOW_DEAL_PREFIX -> {
                if (deal != null) {
                    telegramDealSenderService.sendDealToMe(
                            AUTHENTICATED_USER.getValue(requestContext), deal
                    );
                }
                telegramClient.execute(new DeleteMessage(
                        CHAT_ID.getValue(requestContext),
                        callback.getMessage().getMessageId()
                ));
            }
            case USER_DEAL_AMOUNT_ENTER -> {
                updateUserState(requestContext, UserState.WAITING_AMOUNT);
                userContextService.updateUserContext(authUser, CURRENT_DEAL_ID, deal.getId());
                sendMessage(requestContext, MessageConstants.WAITING_AMOUNT_INPUT_MESSAGE);
            }
            case USER_DEAL_COMMISSION_ENTER -> {
                telegramClient.execute(createSendMessage(
                        CHAT_ID.getValue(requestContext),
                        MessageConstants.WAITING_AMOUNT_CASHED_INPUT_MESSAGE
                ));
                userContextService.updateUserContext(authUser, CURRENT_DEAL_ID, deal.getId());
                userContextService.updateUserContext(
                        AUTHENTICATED_USER.getValue(requestContext),
                        USER_STATE,
                        UserState.WAITING_AMOUNT_CASHED.name()
                );
            }
            case USER_DEAL_APPROVE_CALLBACK_PREFIX -> {
                if (deal.getFlow() == DealFlow.SHOP) {
                    bitrixIntegrationService.inWorkShop(deal.getId());
                } else if (deal.getFlow() == DealFlow.FUNERAL) {
                    bitrixIntegrationService.inWorkFuneral(deal.getId());
                }
                deal.setDealActionStatus(DealActionStatus.WAITING_SECOND_APPROVAL);
                deal = dealRepository.save(deal);
                telegramDealSenderService.notifyPartnerAndAssistantsAboutDealAccepted(
                        AUTHENTICATED_USER.getValue(requestContext), deal
                );
            }
            case USER_DEAL_SECOND_REJECT_CALLBACK_PREFIX -> {
                deal.setDealActionStatus(DealActionStatus.WAITING_REJECTION);
                deal = dealRepository.save(deal);
                var message = sendMessage(requestContext, MessageConstants.REJECT_INPUT_MESSAGE, MenuConstants.createRejectReasonsMenu(deal.getId()));
                var lme = new LinkedMessageEntity();
                lme.setDeal(deal);
                lme.setTelegramUser(AUTHENTICATED_USER.getValue(requestContext));
                lme.setMessageId(message.getMessageId());
                linkedMessagesRepository.save(lme);
            }
            case USER_DEAL_REJECT_CALLBACK_PREFIX -> {
                updateUserState(requestContext, UserState.WAITING_PROBLEM);
                userContextService.updateUserContext(authUser, CURRENT_DEAL_ID, deal.getId());
                sendMessage(requestContext, MessageConstants.PROBLEM_INPUT_MESSAGE);
            }
            case USER_DEAL_SECOND_APPROVE_CALLBACK_PREFIX -> {
                if (deal.getFlow() == DealFlow.SHOP) {
                    bitrixIntegrationService.successDealShop(deal.getId());
                } else if (deal.getFlow() == DealFlow.FUNERAL) {
                    bitrixIntegrationService.successDealFuneral(deal.getId());
                }
                deal.setDealActionStatus(DealActionStatus.WAITING_AMOUNT);
                deal = dealRepository.save(deal);
                telegramDealSenderService.notifyPartnerAndAssistantsAboutDealSuccess(
                        AUTHENTICATED_USER.getValue(requestContext), deal
                );
            }
            case USER_DEAL_SECOND_PHONE_CALLBACK_PREFIX -> {
                bitrixIntegrationService.needPhone(deal.getId());
                telegramDealSenderService.notifyPartnerAndAssistantsAboutDealNeedPhone(
                        AUTHENTICATED_USER.getValue(requestContext), deal
                );
            }
            default -> {
                var rejectReason = Arrays.stream(RejectReason.values())
                        .filter(reason -> reason.getCallbackPrefix().equals(callbackPrefix))
                        .findFirst();
                if (rejectReason.isPresent()) {
                    bitrixIntegrationService.reject(deal.getId(), rejectReason.get().getBitrixValue());
                    dealService.finishDeal(deal.getId(), false);
                    telegramDealSenderService.notifyPartnerAndAssistantsAboutDealReject(
                            AUTHENTICATED_USER.getValue(requestContext),
                            deal,
                            rejectReason.get().getLexem()
                    );
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
    private Message sendMessage(Map<String, Object> requestContext, String message, ReplyKeyboard replyKeyboard) {
        return telegramClient.execute(createSendMessage(
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

    private static String extractCallbackPostfix(String callback) {
        return callback.split("_")[1];
    }

}
