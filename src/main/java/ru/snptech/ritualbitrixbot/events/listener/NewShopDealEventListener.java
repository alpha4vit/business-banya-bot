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
import ru.snptech.ritualbitrixbot.repository.TelegramUserRepository;
import ru.snptech.ritualbitrixbot.telegram.MenuConstants;
import ru.snptech.ritualbitrixbot.telegram.service.TelegramDealSenderService;

@Component
@RequiredArgsConstructor
public class NewShopDealEventListener implements ApplicationListener<NewDealEvent> {
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramDealSenderService telegramDealSenderService;

    @Async
    @Override
    @SneakyThrows
    public void onApplicationEvent(NewDealEvent event) {
        telegramDealSenderService.notifyPartnerAndAssistantsAboutNewDeal(
                telegramUserRepository.findByChatId(Long.valueOf(event.getChatId())),
                event
        );
    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
