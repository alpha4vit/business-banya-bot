package ru.snptech.businessbanyabot.events.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.events.dto.PhoneReceivedEvent;
import ru.snptech.businessbanyabot.repository.DealRepository;
import ru.snptech.businessbanyabot.telegram.service.TelegramDealSenderService;

@Component
@RequiredArgsConstructor
public class PhoneReceivedEventListener implements ApplicationListener<PhoneReceivedEvent> {
    private final TelegramDealSenderService telegramDealSenderService;
    private final DealRepository dealRepository;

    @Async
    @Override
    @SneakyThrows
    public void onApplicationEvent(PhoneReceivedEvent event) {
        var deal = dealRepository.findDealById(event.getDealId());
        if (deal != null) {
            telegramDealSenderService.notifyPartnerAndAssistantsAboutDealPhoneReceived(
                    deal.getTelegramUser(),
                    deal
            );
        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
