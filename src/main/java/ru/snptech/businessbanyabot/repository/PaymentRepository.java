package ru.snptech.businessbanyabot.repository;

import ru.snptech.businessbanyabot.entity.Payment;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.model.payment.PaymentStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    <S extends Payment> Iterable<S> saveAll(Iterable<S> payments);

    Optional<Payment> findById(UUID id);

    Optional<Payment> findByUserChatIdAndStatus(Long chatId, PaymentStatus paymentStatus);

    List<Payment> findAll();

    List<Payment> findAllByUpdatedAtBeforeAndStatus(Instant time, PaymentStatus status);

    Payment findByExternalId(String externalId);

}
