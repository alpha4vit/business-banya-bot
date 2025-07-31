package ru.snptech.businessbanyabot.repository;

import ru.snptech.businessbanyabot.entity.Payment;
import ru.snptech.businessbanyabot.model.payment.PaymentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);

    Optional<Payment> findByUserChatIdAndStatus(Long chatId, PaymentStatus paymentStatus);

    List<Payment> findAll();

}
