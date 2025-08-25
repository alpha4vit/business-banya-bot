package ru.snptech.businessbanyabot.service.report;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.snptech.businessbanyabot.entity.ButtonStatsEntity;
import ru.snptech.businessbanyabot.model.report.CityReportData;
import ru.snptech.businessbanyabot.model.report.ReportType;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.repository.ButtonStatsRepository;
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
public class ButtonStatsReportService implements ReportService {

    private final ButtonStatsRepository buttonStatsRepository;

    @Override
    public File getReport(final ReportType type, final String input) {
        var stats = buttonStatsRepository.findAll();

        return exportToExcelFile(stats);
    }

    public static File exportToExcelFile(List<ButtonStatsEntity> stats) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Button stats");

            String[] headers = {
                "Кнопка",
                "Количество нажатий",
            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            int rowIndex = 1;


            for (var it : stats) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(it.getButtonName());
                row.createCell(1).setCellValue(it.getCount());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String fileName = "button_stats_report_" + Instant.now().toString().replace(":", "-") + ".xlsx";
            File tempFile = File.createTempFile(fileName, ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }

            return tempFile;

        } catch (IOException e) {
            throw new RuntimeException("Failed to export Buttons Stats Report to Excel file", e);
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
