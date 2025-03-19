package ru.snptech.ritualbitrixbot.telegram.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.entity.Deal;
import ru.snptech.ritualbitrixbot.entity.DealActionStatus;
import ru.snptech.ritualbitrixbot.entity.DealFlow;
import ru.snptech.ritualbitrixbot.entity.DealStatus;
import ru.snptech.ritualbitrixbot.entity.LinkedMessageEntity;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;
import ru.snptech.ritualbitrixbot.events.dto.NewDealEvent;
import ru.snptech.ritualbitrixbot.repository.DealRepository;
import ru.snptech.ritualbitrixbot.repository.LinkedMessagesRepository;
import ru.snptech.ritualbitrixbot.repository.TelegramUserRepository;
import ru.snptech.ritualbitrixbot.service.DealService;
import ru.snptech.ritualbitrixbot.telegram.MenuConstants;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static ru.snptech.ritualbitrixbot.telegram.TelegramUtils.escapeMarkdownV2;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramDealSenderService {
    private final TelegramClient telegramClient;
    private final TelegramUserRepository telegramUserRepository;
    private final DealService dealService;
    private final DealRepository dealRepository;
    private final LinkedMessagesRepository linkedMessagesRepository;

    public void notifyPartnerAndAssistantsAboutAmountEntered(TelegramUser currentUser, String dealId, String amount) {
        var deal = dealRepository.findDealById(dealId);
        var sendMessage = SendMessage.builder()
                .chatId(-1L)
                .text(MessageConstants.DEAL_AMOUNT_ENTERED_MESSAGE.formatted(getDealDetailsText(deal), amount))
                .replyMarkup(MenuConstants.createInitDealAmountCashedMenu(dealId))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        notifyPartnerAndAssistants(currentUser, sendMessage, dealId);
    }

    public void notifyPartnerAndAssistantsAboutAmountCashedEntered(TelegramUser currentUser, String dealId, String amount) {
        var deal = dealRepository.findDealById(dealId);
        var sendMessage = SendMessage.builder()
                .chatId(-1L)
                .text(MessageConstants.DEAL_COMMISSION_ENTERED_MESSAGE.formatted(getDealDetailsText(deal), escapeMarkdownV2(amount)))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        notifyPartnerAndAssistants(currentUser, sendMessage, dealId);
    }

    public void notifyPartnerAndAssistantsAboutNewDeal(TelegramUser currentUser, NewDealEvent event) {
        var messageText = MessageConstants.NEW_DEAL_MESSAGE_TEMPLATE.formatted(
                escapeMarkdownV2(event.getDealId()),
                escapeMarkdownV2(event.getSource()),
                escapeMarkdownV2(event.getSource2()),
                escapeMarkdownV2(event.getDealType())
        );
        var sendMessage = SendMessage.builder()
                .chatId(event.getChatId())
                .text(messageText)
                .parseMode(ParseMode.MARKDOWNV2)
                .replyMarkup(
                        MenuConstants.createGetOrCantDealMenu(event.getDealId())
                )
                .build();
        notifyPartnerAndAssistants(currentUser, sendMessage, event.getDealId());
    }

    public void notifyPartnerAndAssistantsAboutDealProblem(TelegramUser currentUser, Deal deal, String reason) {
        var sendMessage = SendMessage.builder()
                .chatId(-1L)
                .text(MessageConstants.DEAL_PROBLEM_MESSAGE.formatted(getDealDetailsText(deal), escapeMarkdownV2(reason)))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        notifyPartnerAndAssistants(currentUser, sendMessage, deal.getId());
    }

    public void notifyPartnerAndAssistantsAboutDealReject(TelegramUser currentUser, Deal deal, String reason) {
        var sendMessage = SendMessage.builder()
                .chatId(-1L)
                .text(MessageConstants.DEAL_REJECT_MESSAGE.formatted(getDealDetailsText(deal), escapeMarkdownV2(reason)))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        notifyPartnerAndAssistants(currentUser, sendMessage, deal.getId());
    }

    public void notifyPartnerAndAssistantsAboutDealFinished(TelegramUser currentUser, Deal deal) {
        var sendMessage = SendMessage.builder()
                .chatId(-1L)
                .text(MessageConstants.DEAL_FINISHED_MESSAGE.formatted(getDealDetailsText(deal)))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        notifyPartnerAndAssistants(currentUser, sendMessage, deal.getId());
    }

    public void notifyPartnerAndAssistantsAboutDealAccepted(TelegramUser currentUser, Deal deal) {
        if (deal.getFlow() == DealFlow.SHOP) {
            var text = MessageConstants.NEW_SHOP_DEAL_MESSAGE_TEMPLATE.formatted(
                    escapeMarkdownV2(deal.getId()),
                    escapeMarkdownV2(deal.getDealType()),
                    escapeMarkdownV2(deal.getSource2()),
                    escapeMarkdownV2(deal.getComment()),
                    escapeMarkdownV2(deal.getContactName()),
                    escapeMarkdownV2(deal.getContactPhone())
            );
            var sendMessage = SendMessage.builder()
                    .chatId(-1L)
                    .text(text)
                    .parseMode(ParseMode.MARKDOWNV2)
                    .replyMarkup(MenuConstants.dealSecondMenu(deal.getId()))
                    .build();
            notifyPartnerAndAssistants(currentUser, sendMessage, deal.getId());
        } else if (deal.getFlow() == DealFlow.FUNERAL) {
            var text = MessageConstants.NEW_FUNERAL_DEAL_MESSAGE_TEMPLATE.formatted(
                    escapeMarkdownV2(deal.getId()),
                    escapeMarkdownV2(deal.getDealType()),
                    escapeMarkdownV2(deal.getSource2()),
                    escapeMarkdownV2(deal.getComment()),
                    escapeMarkdownV2(deal.getAddress()),
                    escapeMarkdownV2(deal.getCity()),
                    escapeMarkdownV2(deal.getDeceasedSurname())
            );
            var sendMessage = SendMessage.builder()
                    .chatId(-1L)
                    .text(text)
                    .parseMode(ParseMode.MARKDOWNV2)
                    .replyMarkup(MenuConstants.funeralMenu(deal.getId()))
                    .build();
            notifyPartnerAndAssistants(currentUser, sendMessage, deal.getId());
        }
    }

    public void notifyPartnerAndAssistantsAboutDealSuccess(TelegramUser currentUser, Deal deal) {
        var sendMessage = SendMessage.builder()
                .chatId(-1L)
                .text(MessageConstants.DEAL_SUCCESS_MESSAGE.formatted(getDealDetailsText(deal)))
                .replyMarkup(MenuConstants.createInitDealAmountMenu(deal.getId()))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        notifyPartnerAndAssistants(currentUser, sendMessage, deal.getId());
    }

    public void notifyPartnerAndAssistantsAboutDealNeedPhone(TelegramUser currentUser, Deal deal) {
        var sendMessage = SendMessage.builder()
                .chatId(-1L)
                .text(MessageConstants.PHONE_REQUESTED_MESSAGE.formatted(getDealDetailsText(deal)))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        notifyPartnerAndAssistants(currentUser, sendMessage, deal.getId());
    }

    public void notifyPartnerAndAssistantsAboutDealPhoneReceived(TelegramUser currentUser, Deal deal) {
        var messageText = MessageConstants.FUNERAL_DEAL_WITH_CONTACTS_MESSAGE_TEMPLATE.formatted(
                escapeMarkdownV2(deal.getId()),
                escapeMarkdownV2(deal.getDealType()),
                escapeMarkdownV2(deal.getSource2()),
                escapeMarkdownV2(deal.getComment()),
                escapeMarkdownV2(deal.getAddress()),
                escapeMarkdownV2(deal.getCity()),
                escapeMarkdownV2(deal.getDeceasedSurname()),
                escapeMarkdownV2(deal.getContactName()),
                escapeMarkdownV2(deal.getContactPhone())
        );
        deal.setDealActionStatus(DealActionStatus.WAITING_SECOND_APPROVAL);
        deal = dealRepository.save(deal);
        var sendMessage = SendMessage.builder()
                .chatId(deal.getTelegramUser().getChatId())
                .text(messageText)
                .parseMode(ParseMode.MARKDOWNV2)
                .replyMarkup(
                        MenuConstants.dealSecondMenu(deal.getId())
                )
                .build();
        notifyPartnerAndAssistants(currentUser, sendMessage, deal.getId());
    }

    @SneakyThrows
    public void sendDealToMe(TelegramUser currentUser, Deal deal) {
        var sendMessage = SendMessage.builder()
                .chatId(currentUser.getChatId())
                .text(getDealDetailsText(deal))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        var replyMarkup = switch (deal.getDealActionStatus()) {
            case WAITING_APPROVAL -> MenuConstants.createGetOrCantDealMenu(deal.getId());
            case WAITING_AMOUNT -> MenuConstants.createInitDealAmountMenu(deal.getId());
            case WAITING_COMMISSION -> MenuConstants.createInitDealAmountCashedMenu(deal.getId());
            case WAITING_REJECTION -> MenuConstants.createRejectReasonsMenu(deal.getId());
            case WAITING_SECOND_APPROVAL ->
                    deal.getFlow() == DealFlow.FUNERAL ? MenuConstants.funeralMenu(deal.getId()) : MenuConstants.dealSecondMenu(deal.getId());
        };
        if (deal.getStatus() == null) {
            sendMessage.setReplyMarkup(replyMarkup);
        }
        var lmeList = linkedMessagesRepository.findByDeal_IdAndTelegramUser_ChatId(
                deal.getId(), currentUser.getChatId()
        );
        lmeList.forEach(linkedMessage -> {
            try {
                telegramClient.execute(new DeleteMessage(currentUser.getChatId().toString(), linkedMessage.getMessageId()));
            } catch (TelegramApiException ignored) {}
        });
        var message = telegramClient.execute(sendMessage);
        var lme = new LinkedMessageEntity();
        lme.setDeal(deal);
        lme.setMessageId(message.getMessageId());
        lme.setTelegramUser(currentUser);
        linkedMessagesRepository.save(lme);
    }

    @SneakyThrows
    public void sendMyStatistic(TelegramUser currentUser) {
        var author = Optional.ofNullable(currentUser.getPartnerAccount()).orElse(currentUser);
        var successDeals = dealRepository.findAllByTelegramUserAndStatusIn(author, Set.of(DealStatus.SUCCESS));
        Map<String, Long> successDealsSummary = successDeals.stream()
                .collect(Collectors.groupingBy(Deal::getDealType, Collectors.counting()));
        var failureDeals = dealRepository.findAllByTelegramUserAndStatusIn(author, Set.of(DealStatus.FAILURE, DealStatus.PROBLEM));
        Map<String, Long> failureDealsSummary = failureDeals.stream()
                .collect(Collectors.groupingBy(Deal::getDealType, Collectors.counting()));
        AtomicReference<String> messageText = new AtomicReference<>("");
        messageText.set("Всего сделок: %d\n\n".formatted(successDeals.size() + failureDeals.size()));
        messageText.set(messageText.get() + "Успешные сделки: %d\n".formatted(successDeals.size()));
        successDealsSummary.forEach((key, value) -> {
            messageText.set(messageText.get() + "%s: %d\n".formatted(key, value));
        });
        messageText.set(messageText.get() + "\nПроваленные сделки: %d\n".formatted(failureDeals.size()));
        failureDealsSummary.forEach((key, value) -> {
            messageText.set(messageText.get() + "%s: %d\n".formatted(key, value));
        });

        var totalAmount = successDeals.stream().mapToLong(deal -> Optional.ofNullable(deal.getAmount()).orElse(BigDecimal.ZERO).longValue()).sum();
        var totalCommission = successDeals.stream().mapToLong(deal -> Optional.ofNullable(deal.getCommission()).orElse(BigDecimal.ZERO).longValue()).sum();

        messageText.set(messageText.get() + "\nОбщая сумма: %d\n".formatted(totalAmount));
        messageText.set(messageText.get() + "\nКомиссия: %d\n".formatted(totalCommission));
        telegramClient.execute(
                SendMessage.builder()
                        .chatId(currentUser.getChatId())
                        .text(messageText.get())
                        .build()
        );
    }

    @SneakyThrows
    public void sendMyActive(TelegramUser currentUser) {
        var author = Optional.ofNullable(currentUser.getPartnerAccount()).orElse(currentUser);
        var deals = dealRepository.findAllByTelegramUserAndStatusIsNull(author);
        var rows = new ArrayList<InlineKeyboardRow>();
        for (int i = 0; i < deals.size(); i++) {
            if (i % 4 == 0) {
                rows.add(new InlineKeyboardRow());
            }
            var currentRow = rows.getLast();
            currentRow.add(InlineKeyboardButton.builder().text(deals.get(i).getId()).callbackData("SD_" + deals.get(i).getId()).build());
        }
        telegramClient.execute(
                SendMessage.builder()
                        .chatId(currentUser.getChatId())
                        .text(rows.isEmpty() ? "У вас нет активных заявок" : "Активные заявки")
                        .replyMarkup(new InlineKeyboardMarkup(rows))
                        .build()
        );
    }

    @SneakyThrows
    public void sendMyClosed(TelegramUser currentUser) {
        var author = Optional.ofNullable(currentUser.getPartnerAccount()).orElse(currentUser);
        var deals = dealRepository.findAllByTelegramUserAndStatusIn(author, List.of(DealStatus.values()));
        var rows = new ArrayList<InlineKeyboardRow>();
        for (int i = 0; i < deals.size(); i++) {
            if (i % 4 == 0) {
                rows.add(new InlineKeyboardRow());
            }
            var currentRow = rows.getLast();
            currentRow.add(InlineKeyboardButton.builder().text(deals.get(i).getId()).callbackData("SD_" + deals.get(i).getId()).build());
        }
        telegramClient.execute(
                SendMessage.builder()
                        .chatId(currentUser.getChatId())
                        .text(rows.isEmpty() ? "У вас нет закрытых заявок" : "Закрытые заявки")
                        .replyMarkup(new InlineKeyboardMarkup(rows))
                        .build()
        );
    }

    private void notifyPartnerAndAssistants(TelegramUser currentUser, SendMessage message, String dealId) {
        getPartnerAndAssistantsTelegramAccount(currentUser).forEach(user -> {
            try {
                linkedMessagesRepository.findByDeal_IdAndTelegramUser_ChatId(dealId, user.getChatId())
                        .stream()
                        .filter(linkedMessageEntity -> linkedMessageEntity.getMessageId() != null)
                        .forEach(linkedMessage -> {
                            try {
                                telegramClient.execute(new DeleteMessage(user.getChatId().toString(), linkedMessage.getMessageId()));
                            } catch (TelegramApiException e) {
                                log.warn(e.getMessage(), e);
                            }
                            linkedMessagesRepository.delete(linkedMessage);
                        });

                log.info("Sending message {} to {}", message, user.getChatId());
                message.setChatId(user.getChatId());
                var result = telegramClient.execute(message);
                LinkedMessageEntity lme = new LinkedMessageEntity();
                lme.setDeal(dealRepository.findById(dealId).get());
                lme.setTelegramUser(user);
                lme.setMessageId(result.getMessageId());
                linkedMessagesRepository.save(lme);

            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<TelegramUser> getPartnerAndAssistantsTelegramAccount(TelegramUser currentUser) {
        return Optional.ofNullable(currentUser.getPartnerAccount())
                .map(partner -> telegramUserRepository.findAllByChatIdOrPartnerAccount_ChatId(partner.getChatId(), partner.getChatId()))
                .orElseGet(
                        () -> telegramUserRepository.findAllByChatIdOrPartnerAccount_ChatId(currentUser.getChatId(), currentUser.getChatId())
                );
    }

    private String getDealDetailsText(Deal deal) {
        var text = "";
        if (deal.getFlow() == DealFlow.SHOP) {
            text = MessageConstants.SHOP_DEAL_MESSAGE_TEMPLATE.formatted(
                    escapeMarkdownV2(deal.getId()),
                    escapeMarkdownV2(deal.getDealType()),
                    escapeMarkdownV2(deal.getSource2()),
                    escapeMarkdownV2(deal.getComment()),
                    escapeMarkdownV2(deal.getContactName()),
                    escapeMarkdownV2(deal.getContactPhone())
            );
        } else {
            text = MessageConstants.FUNERAL_DEAL_MESSAGE_TEMPLATE.formatted(
                    escapeMarkdownV2(deal.getId()),
                    escapeMarkdownV2(deal.getDealType()),
                    escapeMarkdownV2(deal.getSource2()),
                    escapeMarkdownV2(deal.getComment()),
                    escapeMarkdownV2(deal.getAddress()),
                    escapeMarkdownV2(deal.getCity()),
                    escapeMarkdownV2(deal.getDeceasedSurname())
            );
        }
        if (deal.getFlow() == DealFlow.FUNERAL && deal.getRequestedPhone() != null) {
            text += "\n*Клиент \\(Имя\\):* %s".formatted(escapeMarkdownV2(deal.getContactName()));
            text += "\n*Клиент \\(Телефон\\):* %s".formatted(escapeMarkdownV2(deal.getContactPhone()));
        }
        if (deal.getAmount() != null) {
            text += "\nСумма сделки: " + escapeMarkdownV2(deal.getAmount().toPlainString());
        }
        if (deal.getCommission() != null) {
            text += "\nСумма комиссии: " + escapeMarkdownV2(deal.getCommission().toPlainString());
        }
        return text;
    }

}
