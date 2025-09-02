package ru.snptech.businessbanyabot.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.businessbanyabot.model.user.UserStatus;
import ru.snptech.businessbanyabot.repository.UserInfoRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UnbanUsersTask {

    private final UserRepository userRepository;

    @Transactional
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void updateResidents() {
        var users = userRepository.findAll();

        users.forEach(user -> {
            if (user.getBannedAt() != null && user.getBannedAt().plus(Duration.ofDays(93)).isBefore(Instant.now())) {
                user.setBannedAt(null);
                user.setStatus(UserStatus.ACTIVE);
            }
        });

        userRepository.saveAll(users);
    }
}
