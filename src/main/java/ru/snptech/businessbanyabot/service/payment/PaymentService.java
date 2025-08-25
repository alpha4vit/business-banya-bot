package ru.snptech.businessbanyabot.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.snptech.businessbanyabot.entity.Payment;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.integration.bank.properties.BankIntegrationProperties;
import ru.snptech.businessbanyabot.model.payment.PaymentContent;
import ru.snptech.businessbanyabot.model.payment.PaymentStatus;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.model.payment.PaymentMetadata;
import ru.snptech.businessbanyabot.repository.PaymentRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.util.TimeUtils;

import java.time.Instant;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final BankIntegrationProperties bankIntegrationProperties;

    public Payment create(
        TelegramUser user,
        PaymentMetadata metadata,
        String currency,
        PaymentType type,
        PaymentContent content,
        String externalId,
        PaymentStatus status
    ) {
        var ttlInSeconds = bankIntegrationProperties.getQrCodeSettings().getTtl().getSeconds();
        var expiredAt = Instant.now().plusSeconds(ttlInSeconds);

        var payment = Payment.builder()
            .status(status)
            .content(content)
            .type(type)
            .externalId(externalId)
            .amount(metadata.paymentAmount())
            .subscriptionContinuationMonths(metadata.subscriptionDurationInMonths())
            .currency(currency)
            .expiredAt(expiredAt)
            .user(user)
            .build();

        return paymentRepository.save(payment);
    }

    public Optional<Payment> findPending(Long chatId) {
        return paymentRepository.findByUserChatIdAndStatus(chatId, PaymentStatus.PENDING);
    }

    public void applyFastPayment(String externalId) {
        var payment = paymentRepository.findByExternalId(externalId);

        payment.setStatus(PaymentStatus.PAID);
        payment.setUpdatedAt(Instant.now());

        updateSubscriptionIfNeeded(payment.getUser(), payment);

        paymentRepository.save(payment);
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    private void updateSubscriptionIfNeeded(TelegramUser user, Payment payment) {
        var continuationDuration = payment.getSubscriptionContinuationMonths();

        if (continuationDuration == null) return;

        var updatedResidentUntil = TimeUtils.plusMonths(user.getResidentUntil(), continuationDuration);

        user.setResidentUntil(updatedResidentUntil);

        userRepository.save(user);
    }
}
