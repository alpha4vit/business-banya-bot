package ru.snptech.ritualbitrixbot.events.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.entity.Deal;
import ru.snptech.ritualbitrixbot.events.dto.NewDealEvent;
import ru.snptech.ritualbitrixbot.events.dto.PhoneReceivedEvent;
import ru.snptech.ritualbitrixbot.repository.DealRepository;
import ru.snptech.ritualbitrixbot.service.DealService;
import ru.snptech.ritualbitrixbot.telegram.MenuConstants;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;

import static ru.snptech.ritualbitrixbot.telegram.TelegramUtils.escapeMarkdownV2;

@Component
@RequiredArgsConstructor
public class PhoneReceivedEventListener implements ApplicationListener<PhoneReceivedEvent> {
    private final TelegramClient telegramClient;
    private final DealService dealService;
    private final DealRepository dealRepository;

    @Async
    @Override
    @SneakyThrows
    public void onApplicationEvent(PhoneReceivedEvent event) {
        var deal = dealRepository.findDealById(event.getDealId());
        telegramClient.execute(createSendMessage(deal));
    }

    private static SendMessage createSendMessage(Deal deal) {
        var messageText = MessageConstants.FUNERAL_DEAL_WITH_CONTACTS_MESSAGE_TEMPLATE.formatted(
                escapeMarkdownV2(deal.getId()),
                escapeMarkdownV2(deal.getDealType()),
                escapeMarkdownV2(deal.getSource()),
                escapeMarkdownV2(deal.getSource2()),
                escapeMarkdownV2(deal.getComment()),
                escapeMarkdownV2(deal.getAddress()),
                escapeMarkdownV2(deal.getCity()),
                escapeMarkdownV2(deal.getCustomerName()),
                escapeMarkdownV2(deal.getDeceasedSurname()),
                escapeMarkdownV2(deal.getContactName()),
                escapeMarkdownV2(deal.getContactPhone())
        );
        return SendMessage.builder()
                .chatId(deal.getTelegramUser().getChatId())
                .text(messageText)
                .parseMode(ParseMode.MARKDOWNV2)
                .replyMarkup(
                        MenuConstants.dealSecondMenu(deal.getId())
                )
                .build();
    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
