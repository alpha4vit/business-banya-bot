package ru.snptech.businessbanyabot.service.report;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.integration.bitrix.util.LabeledEnumUtil;
import ru.snptech.businessbanyabot.model.report.FamilyStatus;
import ru.snptech.businessbanyabot.model.report.ReportType;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.repository.specification.UserSpecification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class FamilyStatusReportService implements ReportService {

    private final UserRepository userRepository;

    @Override
    public File getReport(final ReportType type, final String input) {
        if (input == null) {
            throw new BusinessBanyaInternalException.REPORT_INPUT_CANNOT_BE_NULL();
        }

        try {
            var familyStatus = LabeledEnumUtil.fromId(FamilyStatus.class, input);

            var users = userRepository.findAll(UserSpecification.hasFamilyStatus(familyStatus.getId()));

            return exportToExcelFile(users);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Ошибка генерации отчета: " + e.getMessage());
        }
    }

    public static File exportToExcelFile(List<TelegramUser> users) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            String[] headers = {
                "Чат ID",
                "Полное имя",
                "Телефон",
                "Описание бизнеса",
                "Основная активность",
                "Основная пассивность",
                "Спорт",
                "Принципы",
                "Музыка",
                "Ключевые слова",
                "Любимые фильмы",
                "Сильные стороны",
                "Достижения",
                "Неудачи",
                "Учителя",
                "Цели на будущее",
                "Телеграм имя пользователя",
                "Имя в Телеграм",
                "Фамилия в Телеграм",
                "Социальные сети",
                "Роль",
                "Статус",
                "Дата создания",
                "Дата блокировки",
                "Внешний ID"
            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            for (int i = 0; i < users.size(); i++) {
                TelegramUser user = users.get(i);
                Row row = sheet.createRow(i + 1);

                var info = user.getInfo();

                row.createCell(0).setCellValue(user.getChatId() != null ? user.getChatId().toString() : "");
                row.createCell(1).setCellValue(user.getFullName() != null ? user.getFullName() : "");
                row.createCell(2).setCellValue(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
                row.createCell(3).setCellValue(info != null && info.businessDescription() != null ? info.businessDescription() : "");
                row.createCell(4).setCellValue(info != null && info.mainActive() != null ? info.mainActive() : "");
                row.createCell(5).setCellValue(info != null && info.mainPassive() != null ? info.mainPassive() : "");
                row.createCell(6).setCellValue(info != null && info.sports() != null ? info.sports().toString() : "");
                row.createCell(7).setCellValue(info != null && info.principles() != null ? info.principles().toString() : "");
                row.createCell(8).setCellValue(info != null && info.music() != null ? info.music() : "");
                row.createCell(9).setCellValue(info != null && info.keywords() != null ? info.keywords() : "");
                row.createCell(10).setCellValue(info != null && info.favoriteMovies() != null ? info.favoriteMovies() : "");
                row.createCell(11).setCellValue(info != null && info.strengths() != null ? info.strengths() : "");
                row.createCell(12).setCellValue(info != null && info.achievements() != null ? info.achievements() : "");
                row.createCell(13).setCellValue(info != null && info.defeats() != null ? info.defeats() : "");
                row.createCell(14).setCellValue(info != null && info.teachers() != null ? info.teachers() : "");
                row.createCell(15).setCellValue(info != null && info.futureGoals() != null ? info.futureGoals() : "");
                row.createCell(16).setCellValue(user.getTelegramUsername() != null ? user.getTelegramUsername() : "");
                row.createCell(17).setCellValue(user.getTelegramFirstName() != null ? user.getTelegramFirstName() : "");
                row.createCell(18).setCellValue(user.getTelegramLastName() != null ? user.getTelegramLastName() : "");
                row.createCell(19).setCellValue(user.getSocialMedia() != null ? user.getSocialMedia() : "");
                row.createCell(20).setCellValue(user.getRole() != null ? user.getRole().name() : "");
                row.createCell(21).setCellValue(user.getStatus() != null ? user.getStatus().name() : "");
                row.createCell(22).setCellValue(user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");
                row.createCell(23).setCellValue(user.getBannedAt() != null ? user.getBannedAt().toString() : "");
                row.createCell(24).setCellValue(user.getExternalId() != null ? user.getExternalId() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String fileName = "users_" + Instant.now().toString().replace(":", "-") + ".xlsx";
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
