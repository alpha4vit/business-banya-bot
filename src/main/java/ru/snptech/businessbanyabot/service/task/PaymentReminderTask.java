package ru.snptech.businessbanyabot.service.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.payment.PaymentMetadata;
import ru.snptech.businessbanyabot.propterties.ApplicationProperties;
import ru.snptech.businessbanyabot.repository.PaymentRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.payment.PaymentCalculator;
import ru.snptech.businessbanyabot.service.util.MoneyUtils;
import ru.snptech.businessbanyabot.service.util.TextUtils;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PaymentReminderTask {

    private final UserRepository userRepository;
    private final TelegramClientAdapter telegramClientAdapter;
    private final ApplicationProperties applicationProperties;
    private final ObjectMapper objectMapper;

    private final Duration period = Duration.ofDays(31);

    @SneakyThrows
    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.DAYS)
    public void remindOutdated() {
        var now = Instant.now();

        var toRemind = userRepository.findAllByResidentUntilBetween(now, now.plus(period));

        for (TelegramUser user : toRemind) {
            var paymentAmount = PaymentCalculator.calculateExtensionCost(
                user.getResidentUntil(),
                applicationProperties.getSubscriptionContinuationDurationInMonths()
            );

            var daysBeforeExpiration = ChronoUnit.DAYS.between(now, user.getResidentUntil());

            var message = MessageConstants.RESIDENT_SUBSCRIPTION_CONTINUATION_TEMPLATE.formatted(
                TextUtils.pluralizeDays(daysBeforeExpiration),
                TextUtils.pluralizeMoths(applicationProperties.getSubscriptionContinuationDurationInMonths()),
                MoneyUtils.withCurrency(paymentAmount, applicationProperties.getPayment().getCurrency())
            );

            var paymentMetadata = objectMapper.writeValueAsString(
                new PaymentMetadata(
                    paymentAmount,
                    applicationProperties.getSubscriptionContinuationDurationInMonths()
                )
            );

            telegramClientAdapter.sendMessage(
                user.getChatId(),
                message,
                MenuConstants.createChoosePaymentMethodMenu(paymentMetadata, applicationProperties.getDeposit().getLegalEntityLink())
            );
        }
    }
}
