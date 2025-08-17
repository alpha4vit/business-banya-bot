package ru.snptech.businessbanyabot.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.model.notification.Notification;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationService {

    private final TelegramClientAdapter telegramClientAdapter;

    public void sendNotification(Notification notification, List<TelegramUser> recipients) {
        recipients.forEach(recipient -> {
            telegramClientAdapter.sendMessage(recipient.getChatId(), notification.getContent());
        });
    }

}
