package ru.snptech.businessbanyabot.service.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@UtilityClass
public class TimeUtil {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ru"));
    private final ZoneId zone = ZoneId.of("Europe/Moscow");

    public static String formatToRussianDate(Instant time) {
        ZonedDateTime zdt = time.atZone(zone);

        return zdt.format(formatter);
    }

}
