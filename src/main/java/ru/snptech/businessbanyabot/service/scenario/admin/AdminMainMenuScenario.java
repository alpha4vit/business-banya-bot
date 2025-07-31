package ru.snptech.businessbanyabot.service.scenario.admin;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.repository.PaymentRepository;
import ru.snptech.businessbanyabot.service.payment.PaymentReportService;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.TG_UPDATE;

@Component
public class AdminMainMenuScenario extends AbstractScenario {

    public static final String PAYMENT_REPORT = "Отчет о оплатах";
    private final PaymentRepository paymentRepository;

    public AdminMainMenuScenario(
        TelegramClientAdapter telegramClientAdapter,
        PaymentRepository paymentRepository
    ) {
        super(telegramClientAdapter);

        this.paymentRepository = paymentRepository;
    }

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);

        if (tgUpdate.getMessage().hasText()) {
            switch (tgUpdate.getMessage().getText()) {
                case null -> {
                    throw new BusinessBanyaInternalException.MESSAGE_HAS_NO_CONTENT();
                }

                case PAYMENT_REPORT -> {
                    var payments = paymentRepository.findAll();

                    var report = PaymentReportService.exportToExcelFile(payments);

                    sendFile(requestContext, report);
                }

                default -> {
                    sendMessage(
                        requestContext,
                        MessageConstants.MAIN_MENU,
                        MenuConstants.createAdminMainMenu()
                    );
                }
            }
        }
    }

}
