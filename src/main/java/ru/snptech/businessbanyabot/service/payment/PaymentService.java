package ru.snptech.businessbanyabot.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.Payment;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.integration.bank.properties.BankIntegrationProperties;
import ru.snptech.businessbanyabot.model.payment.PaymentContent;
import ru.snptech.businessbanyabot.model.payment.PaymentStatus;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.repository.PaymentRepository;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BankIntegrationProperties bankIntegrationProperties;

    public Payment create(
        TelegramUser user,
        Integer amount,
        String currency,
        PaymentType type,
        PaymentContent content,
        String externalId
    ) {
        var ttlInSeconds = bankIntegrationProperties.getQrCodeSettings().getTtl().getSeconds();
        var expiredAt = Instant.now().plusSeconds(ttlInSeconds);

        var payment = Payment.builder()
            .status(PaymentStatus.PENDING)
            .content(content)
            .type(type)
            .externalId(externalId)
            .amount(amount)
            .currency(currency)
            .expiredAt(expiredAt)
            .user(user)
            .build();

        return paymentRepository.save(payment);
    }

    public Optional<Payment> findPending(Long chatId) {
        return paymentRepository.findByUserChatIdAndStatus(chatId, PaymentStatus.PENDING);
    }

}
