package ru.snptech.businessbanyabot.service.scenario.user;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.integration.bank.dto.common.QrType;
import ru.snptech.businessbanyabot.integration.bank.service.BankIntegrationService;
import ru.snptech.businessbanyabot.integration.bank.service.RegisterQrCodeRequestBuilder;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.payment.FastPaymentContent;
import ru.snptech.businessbanyabot.model.payment.PaymentStatus;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.scenario.step.DepositScenarioStep;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.propterties.ApplicationProperties;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.payment.PaymentService;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.service.util.FileUtils;
import ru.snptech.businessbanyabot.service.util.MoneyUtils;
import ru.snptech.businessbanyabot.service.util.TextUtils;
import ru.snptech.businessbanyabot.service.util.TimeUtils;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;
import java.util.UUID;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class PaymentScenario extends AbstractScenario {

    private final RegisterQrCodeRequestBuilder registerQrCodeRequestBuilder;
    private final BankIntegrationService bankIntegrationService;
    private final ApplicationProperties applicationProperties;
    private final UserContextService userContextService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @SneakyThrows
    public void handlePayment(PaymentType type, Map<String, Object> requestContext) {
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

        switch (type) {
            case FAST_PAYMENT -> handleFastPayment(user, requestContext, null);
            case DEPOSIT_FAST_PAYMENT -> handleFastPaymentDeposit(user, requestContext);
            case INVOICE, DEPOSIT_INVOICE -> handleInvoice(user, requestContext);
        }

        PAYMENT_TYPE.setValue(requestContext, type);
        userContextService.updateUserContext(user, requestContext);
    }

    public void balance(Map<String, Object> context) {
        var chatId = CHAT_ID.getValue(context, Long.class);
        var user = userRepository.findByChatId(chatId);

        var balance = TextUtils.balanceToHumanReadable(user.getInfo().getBalance());
        var points = TextUtils.balanceToHumanReadable(user.getInfo().getPoints());

        sendMessage(
            context,
            MessageConstants.BALANCE_MESSAGE.formatted(balance, points),
            MenuConstants.createChooseDepositMethodMenu(chatId)
        );
    }

    public void declinePayment(Map<String, Object> context) {
        var chatId = CHAT_ID.getValue(context, Long.class);
        var user = userRepository.findByChatId(chatId);
        var payment = paymentService.findPending(chatId);

        if (UserRole.NON_RESIDENT.equals(user.getRole())) {
            throw new BusinessBanyaDomainLogicException.INITIAL_PAYMENT_CANNOT_BE_DECLINED();
        }

        payment.ifPresent((it) -> {
            it.setStatus(PaymentStatus.CANCELED);

            paymentService.save(it);
        });
    }

    @SneakyThrows
    private void handleFastPayment(TelegramUser user, Map<String, Object> context, Integer amount) {
        var pendingPayment = paymentService.findPending(user.getChatId());

        if (pendingPayment.isEmpty()) {
            var paymentAmount = amount != null ? amount : applicationProperties.getPayment().getAmount();

            createFastPayment(user, context, paymentAmount);

            return;
        }

        var payment = pendingPayment.get();

        var content = payment.getContentAs(FastPaymentContent.class);

        var file = FileUtils.decodeBase64ToFile(content.getBase64Image());

        var caption = MessageConstants.FAST_PAYMENT_ALREADY_EXISTS_TEMPLATE.formatted(
            MoneyUtils.getHumanReadableAmount(payment.getAmount()),
            payment.getCurrency(),
            payment.getExternalId(),
            TimeUtils.formatToRussianDate(payment.getExpiredAt()),
            content.getLink()
        );

        if (UserRole.RESIDENT.equals(user.getRole())) {
            sendPhoto(context, file, caption, MenuConstants.createDeclinePaymentMenu(payment.getId()));

            return;
        }

        sendPhoto(context, file, caption);
    }


    private void handleInvoice(TelegramUser user, Map<String, Object> context) {
        sendMessage(context, "ССЫЛКА НА ЗАОПЛНЕНИЕ ЗАЯВКУ НА ОПЛАТУ ПО БЕЗНАЛУ");

        SCENARIO.setValue(context, ScenarioType.MAIN_MENU.name());

        userContextService.updateUserContext(user, context);
    }

    private void handleFastPaymentDeposit(TelegramUser user, Map<String, Object> context) {
        var step = SCENARIO_STEP.getValue(context);

        if (DepositScenarioStep.INPUT.name().equals(step)) {
            var update = TG_UPDATE.getValue(context);

            if (!update.hasMessage()) {
                throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
            }

            var depositAmount = getDepositAmount(update.getMessage().getText());

            handleFastPayment(user, context, depositAmount);

            SCENARIO.setValue(context, ScenarioType.MAIN_MENU.name());
            userContextService.updateUserContext(user, context);

            return;
        }

        SCENARIO_STEP.setValue(context, DepositScenarioStep.INPUT.name());

        sendMessage(context, MessageConstants.DEPOSIT_MONEY_INPUT);

        userContextService.updateUserContext(user, context);
    }

    private Integer getDepositAmount(String message) {
        try {
            var deposit = Integer.parseInt(message) * 100;

            var remainder = deposit % applicationProperties.getDeposit().getMultiplicity();

            if (remainder == 0) {
                return deposit;
            }

            throw new IllegalArgumentException();
        } catch (Throwable t) {
            throw new BusinessBanyaDomainLogicException.INVALID_DEPOSIT_AMOUNT(
                MoneyUtils.getHumanReadableAmount(applicationProperties.getDeposit().getMultiplicity())
            );
        }
    }

    @SneakyThrows
    private void createFastPayment(TelegramUser user, Map<String, Object> context, Integer amount) {
        var fastPaymentData = registerQrCodeRequestBuilder.build(QrType.DYNAMIC, amount);

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
        var file = FileUtils.decodeBase64ToFile(base64QrCode);

        var caption = MessageConstants.FAST_PAYMENT_TEMPLATE.formatted(
            MoneyUtils.getHumanReadableAmount(fastPaymentData.data().amount()),
            fastPaymentData.data().currency(),
            bankResponse.data().qrcId(),
            TimeUtils.formatToRussianDate(createdPayment.getExpiredAt()),
            bankResponse.data().payload()
        );

        sendPhoto(context, file, caption);
    }

    public PaymentScenario(
        RegisterQrCodeRequestBuilder registerQrCodeRequestBuilder,
        BankIntegrationService bankIntegrationService,
        UserContextService userContextService,
        PaymentService paymentService,
        UserRepository userRepository,
        TelegramClientAdapter telegramClientAdapter,
        ApplicationProperties applicationProperties
    ) {
        super(telegramClientAdapter);
        this.applicationProperties = applicationProperties;
        this.registerQrCodeRequestBuilder = registerQrCodeRequestBuilder;
        this.bankIntegrationService = bankIntegrationService;
        this.userContextService = userContextService;
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }
}
