package ru.snptech.businessbanyabot.service.task;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.repository.PaymentRepository;
import ru.snptech.businessbanyabot.service.util.MoneyUtils;
import ru.snptech.businessbanyabot.service.util.TimeUtils;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static ru.snptech.businessbanyabot.service.util.FileUtils.decodeBase64ToFile;

@Component
@RequiredArgsConstructor
public class PaymentExpiredTask {

    private final PaymentRepository paymentRepository;
    private final TelegramClientAdapter telegramClientAdapter;

    private final Duration period = Duration.ofDays(3);

    @SneakyThrows
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void remindOutdated() {
        var now = Instant.now().minus(period);

        var outdated = paymentRepository.findAllByUpdatedAtBefore(now);

        outdated.forEach((payment -> {
                var base64QrCode = payment.getContent().toString();
                try {
                    var file = decodeBase64ToFile(base64QrCode);

                    var caption = MessageConstants.FAST_PAYMENT_TEMPLATE.formatted(
                        MoneyUtils.getHumanReadableAmount(payment.getAmount()),
                        payment.getCurrency(),
                        payment.getExternalId(),
                        TimeUtils.formatToRussianDate(payment.getExpiredAt()),
                        payment.getContent().getExternalPayload()
                    );


                    telegramClientAdapter.sendPhoto(payment.getUser().getChatId(), file, caption);

                    payment.setUpdatedAt(now);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
        );


        paymentRepository.saveAll(outdated);
    }
}
