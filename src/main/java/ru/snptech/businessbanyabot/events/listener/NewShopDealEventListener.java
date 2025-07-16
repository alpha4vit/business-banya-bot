package ru.snptech.businessbanyabot.events.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.events.dto.NewDealEvent;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.telegram.service.TelegramDealSenderService;

@Component
@RequiredArgsConstructor
public class NewShopDealEventListener implements ApplicationListener<NewDealEvent> {
    private final UserRepository userRepository;
    private final TelegramDealSenderService telegramDealSenderService;

    @Async
    @Override
    @SneakyThrows
    public void onApplicationEvent(NewDealEvent event) {
        telegramDealSenderService.notifyPartnerAndAssistantsAboutNewDeal(
                userRepository.findByChatId(Long.valueOf(event.getChatId())),
                event
        );
    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
