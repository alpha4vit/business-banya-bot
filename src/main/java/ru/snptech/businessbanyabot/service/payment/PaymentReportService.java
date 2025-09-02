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

            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle amountStyle = createAmountStyle(workbook);

            for (int i = 0; i < payments.size(); i++) {
                Payment payment = payments.get(i);
                Row row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(payment.getId().toString());
                row.createCell(1).setCellValue(payment.getExternalId());
                row.createCell(2).setCellValue(payment.getType().toHumanReadable());
                row.createCell(3).setCellValue(payment.getStatus().toHumanReadable());
                row.createCell(5).setCellValue(payment.getCurrency());
                row.createCell(6).setCellValue(payment.getUser().getFullName());

                Cell amountCell = row.createCell(4);
                amountCell.setCellValue(payment.getAmount().doubleValue() / 100);
                amountCell.setCellStyle(amountStyle);

                Cell createdAtCell = row.createCell(7);
                createdAtCell.setCellValue(java.util.Date.from(payment.getCreatedAt())); // если Instant
                createdAtCell.setCellStyle(dateStyle);

                Cell expiredAtCell = row.createCell(8);
                expiredAtCell.setCellValue(java.util.Date.from(payment.getExpiredAt()));
                expiredAtCell.setCellStyle(dateStyle);
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

    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy HH:mm"));
        return style;
    }

    private static CellStyle createAmountStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
        return style;
    }

}
