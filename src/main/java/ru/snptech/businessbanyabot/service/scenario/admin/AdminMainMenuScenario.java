package ru.snptech.businessbanyabot.service.scenario.admin;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;
import ru.snptech.businessbanyabot.model.scenario.step.NotificationScenarioStep;
import ru.snptech.businessbanyabot.model.scenario.step.ReportScenarioStep;
import ru.snptech.businessbanyabot.repository.PaymentRepository;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.leaderboard.LeaderboardService;
import ru.snptech.businessbanyabot.service.payment.PaymentReportService;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.scenario.report.ReportScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;
import java.util.Set;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class AdminMainMenuScenario extends AbstractScenario {

    public static final String PAYMENT_REPORT = "Отчет о оплатах";
    public static final String NOTIFICATION = "Рассылка";
    public static final String REPORT = "Отчеты";
    public static final String LEADERBOARD = "Рейтинг";

    public static final Set<String> MAIN_MENU_COMMANDS = Set.of(
        PAYMENT_REPORT, NOTIFICATION
    );

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final AdminNotificationScenario adminNotificationScenario;
    private final UserContextService userContextService;
    private final ReportScenario reportScenario;
    private final LeaderboardService leaderboardService;

    public AdminMainMenuScenario(
        TelegramClientAdapter telegramClientAdapter,
        PaymentRepository paymentRepository,
        AdminNotificationScenario adminNotificationScenario,
        UserContextService userContextService,
        UserRepository userRepository,
        ReportScenario reportScenario,
        LeaderboardService leaderboardService
    ) {
        super(telegramClientAdapter);

        this.reportScenario = reportScenario;
        this.adminNotificationScenario = adminNotificationScenario;
        this.paymentRepository = paymentRepository;
        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.leaderboardService = leaderboardService;
    }

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext) {
        var tgUpdate = TG_UPDATE.getValue(requestContext);
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

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

                case NOTIFICATION -> {
                    SCENARIO.setValue(requestContext, ScenarioType.NOTIFICATION.name());
                    SCENARIO_STEP.setValue(requestContext, NotificationScenarioStep.INIT.name());

                    userContextService.updateUserContext(user, requestContext);

                    adminNotificationScenario.invoke(requestContext);
                }

                case REPORT -> {
                    SCENARIO.setValue(requestContext, ScenarioType.REPORT.name());
                    SCENARIO_STEP.setValue(requestContext, ReportScenarioStep.INIT.name());

                    userContextService.updateUserContext(user, requestContext);

                    reportScenario.invoke(requestContext, null);
                }

                case LEADERBOARD -> {
                    var leaderboard = leaderboardService.getLeaderboard(requestContext);

                    sendMessage(requestContext, leaderboard);
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
