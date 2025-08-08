package ru.snptech.businessbanyabot.service.report;

import lombok.RequiredArgsConstructor;
import ru.snptech.businessbanyabot.model.report.ReportType;

import java.io.File;
import java.util.Map;

@RequiredArgsConstructor
public class DispatchingReportService implements ReportService {

    private final Map<ReportType, ReportService> handlers;

    public File getReport(ReportType type, String input) {
        return handlers.get(type).getReport(type, input);
    }

}
