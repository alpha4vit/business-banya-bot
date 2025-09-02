package ru.snptech.businessbanyabot.integration.bitrix.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.snptech.businessbanyabot.model.payment.InvoiceContent;
import ru.snptech.businessbanyabot.model.payment.PaymentMetadata;
import ru.snptech.businessbanyabot.model.payment.PaymentStatus;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.notification.NotificationService;
import ru.snptech.businessbanyabot.service.payment.PaymentService;
import ru.snptech.businessbanyabot.service.util.MoneyUtils;

@Slf4j
@RestController
@RequestMapping("/bitrix")
@RequiredArgsConstructor
public class BitrixController {

    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    @SneakyThrows
    @GetMapping("/payment/{companyId}/{paymentId}/{money}/{paidAt}")
    public void handlePaymentUpdate(
        @PathVariable("companyId") String companyId,
        @PathVariable("paymentId") String paymentId,
        @PathVariable("money") String moneyStr,
        @PathVariable("paidAt") String paidAt,
        @RequestBody String body
    ) {
        var user = userRepository.findByExternalId(companyId);

        if (user.isEmpty()) return;

        var money = MoneyUtils.parse(moneyStr);

        if (money == null) {
            notificationService.notifyAdmin("Сумма оплаты счета не может быть пустой!");

            return;
        }

        paymentService.create(
            user.get(),
            new PaymentMetadata(
                money.amount(),
                null
            ),
            money.currency(),
            PaymentType.INVOICE,
            new InvoiceContent(),
            paymentId,
            PaymentStatus.PAID
        );
    }
}
