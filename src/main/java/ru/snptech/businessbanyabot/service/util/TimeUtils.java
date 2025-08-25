package ru.snptech.businessbanyabot.service.util;

import lombok.experimental.UtilityClass;
import ru.snptech.businessbanyabot.integration.bank.dto.common.WeekDay;

import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

@UtilityClass
public class TimeUtils {

    private static final ZoneId zone = ZoneId.of("Europe/Moscow");

    private static final DateTimeFormatter russianFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ru"));
    private static final DateTimeFormatter fileNameFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm").withZone(zone);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatToRussianDate(Instant time) {
        ZonedDateTime zdt = time.atZone(zone);

        return zdt.format(russianFormatter);
    }

    public static String formatToFileName(Instant time) {
        return fileNameFormatter.format(time);
    }

    public static Instant parseInstant(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            return Instant.parse(dateStr);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static Instant plusMonths(Instant time, Integer months) {
        return time.atZone(zone)
            .toLocalDate()
            .plusMonths(months)
            .atStartOfDay(zone)
            .toInstant();
    }

    public static LocalTime parseTime(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            return LocalTime.parse(dateStr);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static WeekDay getCurrentWeekDay() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();

        return WeekDay.valueOf(dayOfWeek.name());
    }
}
