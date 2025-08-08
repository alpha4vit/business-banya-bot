package ru.snptech.businessbanyabot.service.report;

import ru.snptech.businessbanyabot.model.report.ReportType;

import java.io.File;

public interface ReportService {

    File getReport(ReportType type, String input);

}
