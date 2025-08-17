package ru.snptech.businessbanyabot.repository.impl;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.snptech.businessbanyabot.entity.Payment;
import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.model.payment.PaymentStatus;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;
import ru.snptech.businessbanyabot.repository.PaymentRepository;
import ru.snptech.businessbanyabot.repository.SurveyRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JdbcPaymentRepository extends PaymentRepository, CrudRepository<Payment, UUID> {

    Optional<Payment> findByUserChatIdAndStatus(Long chatId, PaymentStatus paymentStatus);

    List<Payment> findAllByUpdatedAtBefore(Instant time);

    Payment findByExternalId(String externalId);
}