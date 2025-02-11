package ru.snptech.ritualbitrixbot.events.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.events.dto.NewDealEvent;
import ru.snptech.ritualbitrixbot.telegram.MenuConstants;
import ru.snptech.ritualbitrixbot.telegram.MessageConstants;

import static ru.snptech.ritualbitrixbot.telegram.TelegramUtils.escapeMarkdownV2;

@Component
@RequiredArgsConstructor
public class NewShopDealEventListener implements ApplicationListener<NewDealEvent> {
    private final TelegramClient telegramClient;

    @Async
    @Override
    @SneakyThrows
    public void onApplicationEvent(NewDealEvent event) {
        var messageText = MessageConstants.NEW_DEAL_MESSAGE_TEMPLATE.formatted(
                escapeMarkdownV2(event.getDealId()),
                escapeMarkdownV2(event.getSource()),
                escapeMarkdownV2(event.getSource2()),
                escapeMarkdownV2(event.getDealType())
        );
        telegramClient.execute(createSendMessage(event, messageText));
    }

    private static SendMessage createSendMessage(NewDealEvent event, String messageText) {
        return SendMessage.builder()
                .chatId(event.getChatId())
                .text(messageText)
                .parseMode(ParseMode.MARKDOWNV2)
                .replyMarkup(
                        MenuConstants.createGetOrCantDealMenu(event.getDealId())
                )
                .build();
    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
