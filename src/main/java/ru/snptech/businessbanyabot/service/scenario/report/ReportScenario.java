package ru.snptech.businessbanyabot.service.scenario.report;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.integration.bitrix.util.LabeledEnumUtil;
import ru.snptech.businessbanyabot.model.common.MenuConstants;
import ru.snptech.businessbanyabot.model.common.MessageConstants;
import ru.snptech.businessbanyabot.model.report.ReportType;
import ru.snptech.businessbanyabot.model.scenario.step.ReportScenarioStep;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.report.ReportService;
import ru.snptech.businessbanyabot.service.scenario.AbstractScenario;
import ru.snptech.businessbanyabot.service.user.UserContextService;
import ru.snptech.businessbanyabot.telegram.client.TelegramClientAdapter;

import java.util.Map;

import static ru.snptech.businessbanyabot.model.common.ServiceConstantHolder.*;

@Component
public class ReportScenario extends AbstractScenario {

    private final UserContextService userContextService;
    private final UserRepository userRepository;
    private final ReportService reportService;

    @SneakyThrows
    public void invoke(Map<String, Object> requestContext, String input) {
        var chatId = CHAT_ID.getValue(requestContext, Long.class);
        var user = userRepository.findByChatId(chatId);

        var reportStep = SCENARIO_STEP.getValue(requestContext, ReportScenarioStep.class);

        switch (reportStep) {
            case INIT -> {
                SCENARIO_STEP.setValue(requestContext, ReportScenarioStep.REPORT_TYPE_SELECT.name());

                sendMessage(
                    requestContext,
                    MessageConstants.SELECT_REPORT_TYPE_MESSAGE,
                    MenuConstants.createSelectReportType(ReportType.ALL)
                );

                userContextService.updateUserContext(user, requestContext);
            }

            case REPORT_TYPE_SELECT -> {
                var reportType = LabeledEnumUtil.fromId(ReportType.class, input);

                REPORT_TYPE.setValue(requestContext, reportType);

                if (reportType.getEnumClass() == null) {
                    var report = reportService.getReport(reportType, null);

                    sendFile(requestContext, report);

                    return;
                }

                SCENARIO_STEP.setValue(requestContext, ReportScenarioStep.TYPE_PARAM_SELECT.name());

                var params = reportType.getEnumValues();

                if (params == null) {
                    throw new IllegalStateException("Report type params cannot be null on this step");
                }

                sendMessage(
                    requestContext,
                    MessageConstants.SELECT_REPORT_TYPE_PARAM_MESSAGE,
                    MenuConstants.createSelectReportParam(params)
                );

                userContextService.updateUserContext(user, requestContext);
            }

            case TYPE_PARAM_SELECT -> {
                var reportType = REPORT_TYPE.getValue(requestContext, ReportType.class);

                var report = reportService.getReport(reportType, input);

                sendFile(requestContext, report);
            }
        }
    }

    public ReportScenario(
        TelegramClientAdapter telegramClientAdapter,
        UserContextService userContextService,
        UserRepository userRepository,
        ReportService reportService
    ) {
        super(telegramClientAdapter);

        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.reportService = reportService;
    }
}