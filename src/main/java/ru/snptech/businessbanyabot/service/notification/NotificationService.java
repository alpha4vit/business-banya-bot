package ru.snptech.businessbanyabot.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.model.notification.Notification;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.repository.specification.UserSpecification;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final TelegramClientAdapter telegramClientAdapter;

    public void sendNotification(Notification notification, List<TelegramUser> recipients) {
        recipients.forEach(recipient ->
            telegramClientAdapter.sendMessage(recipient.getChatId(), notification.getContent())
        );
    }

    public void notifyAdmin(String message) {
        var admins = userRepository.findAll(UserSpecification.hasRole(UserRole.ADMIN));

        admins.forEach(admin ->
            telegramClientAdapter.sendMessage(admin.getChatId(), message)
        );
    }
}
