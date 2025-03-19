package ru.snptech.ritualbitrixbot.telegram.scenario.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.snptech.ritualbitrixbot.entity.Deal;
import ru.snptech.ritualbitrixbot.entity.DealStatus;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;
import ru.snptech.ritualbitrixbot.repository.DealRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserReportGenerationScenario {
    private final TelegramClient telegramClient;
    private final DealRepository dealRepository;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void invoke(TelegramUser user) {
        executor.submit(() -> {

            try {
                log.info("Start report generatation for user: {}", user.getChatId());
                createAndSendReport(user);
                log.info("Finish report generation for user: {}", user.getChatId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }


        });

    }

    private void createAndSendReport(TelegramUser user) throws IOException, TelegramApiException {
        var partner = Optional.ofNullable(user.getPartnerAccount())
                .map(TelegramUser::getPartnerAccount)
                .orElse(user);
        var deals = dealRepository.findAllByTelegramUser(partner);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        addHeader(sheet);
        deals.forEach(deal -> addDealRow(sheet, deal));

        var baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        telegramClient.execute(SendDocument.builder().document(new InputFile(
                new ByteArrayInputStream(baos.toByteArray()), "Отчет о сделках.xlsx"
        )).chatId(user.getChatId()).build());
    }

    private void addHeader(Sheet sheet) {
        var row = sheet.createRow(0);
        row.createCell(0).setCellValue("ID заявки");
        row.createCell(1).setCellValue("Статус заявки");
        row.createCell(2).setCellValue("Источник 2");
        row.createCell(3).setCellValue("Тип сделки");
        row.createCell(4).setCellValue("Комментарий");
        row.createCell(5).setCellValue("Телефон контакта");
        row.createCell(6).setCellValue("Имя контакта");
        row.createCell(7).setCellValue("Адрес");
        row.createCell(8).setCellValue("Город");
        row.createCell(9).setCellValue("Регион");
        row.createCell(10).setCellValue("Сумма сделки");
        row.createCell(11).setCellValue("Комиссия");
        row.createCell(12).setCellValue("Дата импорта заявки");
    }

    private void addDealRow(Sheet sheet, Deal deal) {
        var row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(0).setCellValue(deal.getId());
        row.createCell(1).setCellValue(Optional.ofNullable(deal.getStatus()).map(DealStatus::getLexem).orElse("В работе"));
        row.createCell(2).setCellValue(deal.getSource2());
        row.createCell(3).setCellValue(deal.getDealType());
        row.createCell(4).setCellValue(deal.getComment());
        row.createCell(5).setCellValue(deal.getContactPhone());
        row.createCell(6).setCellValue(deal.getContactName());
        row.createCell(7).setCellValue(deal.getAddress());
        row.createCell(8).setCellValue(deal.getCity());
        row.createCell(9).setCellValue(deal.getRegion());
        row.createCell(10).setCellValue(Optional.ofNullable(deal.getAmount()).map(BigDecimal::toEngineeringString).orElse(null));
        row.createCell(11).setCellValue(Optional.ofNullable(deal.getCommission()).map(BigDecimal::toEngineeringString).orElse(null));
        row.createCell(12).setCellValue(Optional.ofNullable(deal.getCreatedAt()).map(ts -> ts.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).orElse(null));
    }


}
