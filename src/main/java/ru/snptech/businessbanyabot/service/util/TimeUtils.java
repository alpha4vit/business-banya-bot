package ru.snptech.businessbanyabot.service.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@UtilityClass
public class TimeUtils {

    private static final ZoneId zone = ZoneId.of("Europe/Moscow");

    private static final DateTimeFormatter russianFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ru"));
    private static final DateTimeFormatter fileNameFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm").withZone(zone);

    public static String formatToRussianDate(Instant time) {
        ZonedDateTime zdt = time.atZone(zone);

        return zdt.format(russianFormatter);
    }

    public static String formatToFileName(Instant time) {
        return fileNameFormatter.format(time);
    }

}
