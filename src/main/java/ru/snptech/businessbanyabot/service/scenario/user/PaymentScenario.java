package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.integrations.bank.dto.common.QrType;
import ru.snptech.businessbanyabot.integrations.bank.service.BankIntegrationService;
import ru.snptech.businessbanyabot.integrations.bank.service.RegisterQrCodeRequestBuilder;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.payment.FastPaymentContent;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.payment.PaymentService;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.service.util.ImageUtil;
import ru.snptech.businessbanyabot.service.util.TimeUtil;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.CHAT_ID;
import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.PAYMENT_TYPE;

@Component
public class PaymentScenario extends AbstractScenario {

    private final RegisterQrCodeRequestBuilder registerQrCodeRequestBuilder;
    private final BankIntegrationService bankIntegrationService;
    private final UserContextService userContextService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @SneakyThrows
    public void handle(PaymentType type, Map<String, Object> requestContext) {
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

        switch (type) {
            case FAST_PAYMENT -> handleFastPayment(user, requestContext);
            case INVOICE -> handleInvoice(user, requestContext);
        }

        PAYMENT_TYPE.setValue(requestContext, type);
        userContextService.updateUserContext(user, requestContext);
    }

    @SneakyThrows
    private void handleFastPayment(TelegramUser user, Map<String, Object> context) {
        var pendingPayment = paymentService.findPending(user.getChatId());

        if (pendingPayment.isPresent()) {
            var payment = pendingPayment.get();

            var content = payment.getContentAs(FastPaymentContent.class);

            var file = ImageUtil.decodeBase64ToFile(content.getBase64Image());

            var caption = MessageConstants.FAST_PAYMENT_TEMPLATE.formatted(
                payment.getAmount().toString(),
                payment.getCurrency(),
                payment.getExternalId(),
                TimeUtil.formatToRussianDate(payment.getExpiredAt()),
                content.getLink()
            );

            sendFile(context, file, caption);

            return;
        }

        var fastPaymentData = registerQrCodeRequestBuilder.build(QrType.DYNAMIC);

        var bankResponse = bankIntegrationService.registerQrCode(fastPaymentData);

        var paymentContent = new FastPaymentContent(
            PaymentType.FAST_PAYMENT,
            bankResponse.data().image().content(),
            bankResponse.data().payload()
        );

        var createdPayment = paymentService.create(
            user,
            fastPaymentData.data().amount(),
            fastPaymentData.data().currency(),
            PaymentType.FAST_PAYMENT,
            paymentContent,
            bankResponse.data().qrcId()
        );

        var base64QrCode = bankResponse.data().image().content();
        var file = ImageUtil.decodeBase64ToFile(base64QrCode);

        var caption = MessageConstants.FAST_PAYMENT_TEMPLATE.formatted(
            fastPaymentData.data().amount().toString(),
            fastPaymentData.data().currency(),
            bankResponse.data().qrcId(),
            TimeUtil.formatToRussianDate(createdPayment.getExpiredAt()),
            bankResponse.data().payload()
        );

        sendFile(context, file, caption);
    }

    private void handleInvoice(TelegramUser user, Map<String, Object> context) {
        sendMessage(context, "ССЫЛКА НА ЗАОПЛНЕНИЕ ЗАЯВКУ НА ОПЛАТУ ПО БЕЗНАЛУ");
    }

    public PaymentScenario(
        RegisterQrCodeRequestBuilder registerQrCodeRequestBuilder,
        BankIntegrationService bankIntegrationService,
        UserContextService userContextService,
        PaymentService paymentService,
        UserRepository userRepository,
        TelegramClientAdapter telegramClientAdapter
    ) {
        super(telegramClientAdapter);
        this.registerQrCodeRequestBuilder = registerQrCodeRequestBuilder;
        this.bankIntegrationService = bankIntegrationService;
        this.userContextService = userContextService;
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }
}
