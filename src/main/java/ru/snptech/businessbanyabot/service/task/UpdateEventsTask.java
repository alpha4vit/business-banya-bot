package ru.snptech.businessbanyabot.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.Event;
import ru.snptech.businessbanyabot.integration.bank.dto.common.WeekDay;
import ru.snptech.businessbanyabot.integration.bitrix.dto.event.BitrixEventDto;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.businessbanyabot.repository.EventRepository;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.snptech.businessbanyabot.service.util.TimeUtils.parseInstant;

@Component
@RequiredArgsConstructor
public class UpdateEventsTask {

    private final BitrixIntegrationService bitrixIntegrationService;
    private final EventRepository eventRepository;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES, initialDelay = 0)
    public void updateResidents() {
        var events = bitrixIntegrationService.findAllEvents()
            .stream()
            .collect(Collectors.toMap(
                BitrixEventDto::id,
                Function.identity()
            ));

        var externalIds = events.values()
            .stream()
            .map(BitrixEventDto::id)
            .toList();

        var existed = eventRepository.findByExternalIdIn(externalIds);

        existed.forEach((event) -> {
            var updated = events.get(event.getExternalId());

            updateIfNotNull(event, parseInstant(updated.carryDate()), Function.identity(), Event::setCarryDate);
            updateIfNotNull(event, WeekDay.fromValue(updated.weekDay()), Function.identity(), Event::setWeekDay);
            // TODO add on bitrix
            updateIfNotNull(event, LocalTime.now().plusHours(10), Function.identity(), Event::setTime);
            updateIfNotNull(event, updated.photo(), Function.identity(), Event::setPhoto);
            updateIfNotNull(event, updated.fullDescription(), Function.identity(), Event::setFullDescription);
            updateIfNotNull(event, updated.previewDescription(), Function.identity(), Event::setPreviewDescription);
            updateIfNotNull(event, updated.tableLink(), Function.identity(), Event::setTableLink);
            updateIfNotNull(event, updated.registrationLink(), Function.identity(), Event::setRegistrationLink);
        });

        var existingIds = existed.stream()
            .map(Event::getExternalId)
            .collect(Collectors.toSet());

        var newEvents = events.values().stream()
            .filter(dto -> !existingIds.contains(dto.id()))
            .map(this::mapToEvent)
            .toList();

        eventRepository.saveAll(newEvents);
    }

    public <T, R> void updateIfNotNull(Event event, T value, Function<T, R> mapper, BiConsumer<Event, R> setter) {
        if (value != null) {
            setter.accept(event, mapper.apply(value));
        }
    }

    public Event mapToEvent(BitrixEventDto dto) {
        return Event.builder()
            .externalId(dto.id())
            .title(dto.title())
            .speaker(dto.speaker())
            .carryDate(parseInstant(dto.carryDate()))
            .weekDay(WeekDay.fromValue(dto.weekDay()))
            .time(LocalTime.now().plusHours(10)) // TODO add to bitrix, parse by TimeUtils
            .fullDescription(dto.fullDescription())
            .previewDescription(dto.previewDescription())
            .photo(dto.photo())
            .registrationLink(dto.registrationLink())
            .tableLink(dto.tableLink())
            .build();
    }
}
