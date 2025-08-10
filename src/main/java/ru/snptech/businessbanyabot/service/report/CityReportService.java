package ru.snptech.businessbanyabot.service.report;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.integration.bitrix.util.LabeledEnumUtil;
import ru.snptech.businessbanyabot.model.report.CityReportData;
import ru.snptech.businessbanyabot.model.report.RecruitmentWays;
import ru.snptech.businessbanyabot.model.report.ReportType;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.repository.specification.UserSpecification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CityReportService implements ReportService {

    private final UserRepository userRepository;

    @Override
    public File getReport(final ReportType type, final String input) {
        var users = userRepository.findAll(UserSpecification.hasRole(UserRole.RESIDENT));


        var cityReportData = users.stream()
            .collect(Collectors.groupingBy(
                user -> user.getInfo().getCity(),
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    usersInCity -> {
                        int yearTurnoverSum = usersInCity.stream()
                            .mapToInt(u -> u.getInfo().getAnnualTurnover() != null ? Integer.parseInt(u.getInfo().getAnnualTurnover()) : 0)
                            .sum();

                        int averageIncomeAvg = (int) usersInCity.stream()
                            .filter(u -> u.getInfo().getAverageMonthlyIncome() != null)
                            .mapToInt(u -> Integer.parseInt(u.getInfo().getAverageMonthlyIncome()))
                            .average()
                            .orElse(0);

                        int usersCount = usersInCity.size();

                        return new CityReportData(yearTurnoverSum, averageIncomeAvg, usersCount);
                    }
                )
            ));

        return exportToExcelFile(cityReportData);
    }

    public static File exportToExcelFile(Map<String, CityReportData> cityReport) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Cities");

            String[] headers = {
                "Город",
                "Годовой оборот",
                "Средний доход",
                "Количество пользователей"
            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            int rowIndex = 1;
            for (Map.Entry<String, CityReportData> entry : cityReport.entrySet()) {
                Row row = sheet.createRow(rowIndex++);

                String city = entry.getKey();
                CityReportData data = entry.getValue();

                row.createCell(0).setCellValue(city != null ? city : "");
                row.createCell(1).setCellValue(data.annualTurnover() != null ? data.annualTurnover() : 0);
                row.createCell(2).setCellValue(data.averageMonthlyIncome() != null ? data.averageMonthlyIncome() : 0);
                row.createCell(3).setCellValue(data.usersCount() != null ? data.usersCount() : 0);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String fileName = "city_report_" + Instant.now().toString().replace(":", "-") + ".xlsx";
            File tempFile = File.createTempFile(fileName, ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }

            return tempFile;

        } catch (IOException e) {
            throw new RuntimeException("Failed to export City Report Excel file", e);
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
}
