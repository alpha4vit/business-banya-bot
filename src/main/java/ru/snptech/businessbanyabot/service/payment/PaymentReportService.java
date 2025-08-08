package ru.snptech.businessbanyabot.service.payment;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.snptech.businessbanyabot.entity.Payment;
import ru.snptech.businessbanyabot.service.util.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class PaymentReportService {

    public static File exportToExcelFile(List<Payment> payments) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Payments");

            Row header = sheet.createRow(0);
            String[] headers = {
                "Номер", "Внешний номер", "Тип", "Статус", "Сумма",
                "Валюта", "ФИО", "Дата создания", "Дата истечения"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            for (int i = 0; i < payments.size(); i++) {
                Payment payment = payments.get(i);
                Row row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(payment.getId().toString());
                row.createCell(1).setCellValue(payment.getExternalId());
                row.createCell(2).setCellValue(payment.getType().toHumanReadable());
                row.createCell(3).setCellValue(payment.getStatus().toHumanReadable());
                row.createCell(4).setCellValue(payment.getAmount());
                row.createCell(5).setCellValue(payment.getCurrency());
                row.createCell(6).setCellValue(payment.getUser().getFullName());
                row.createCell(7).setCellValue(payment.getCreatedAt().toString());
                row.createCell(8).setCellValue(payment.getExpiredAt().toString());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            var fileName = "оплаты_%s_".formatted(TimeUtils.formatToFileName(Instant.now()));

            File tempFile = File.createTempFile(fileName, ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }

            return tempFile;

        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel file", e);
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
