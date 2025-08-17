package ru.snptech.businessbanyabot.repository;

import org.springframework.data.jpa.repository.Query;
import ru.snptech.businessbanyabot.entity.Event;
import ru.snptech.businessbanyabot.integration.bank.dto.common.WeekDay;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface EventRepository {

    <S extends Event> Iterable<S> saveAll(Iterable<S> events);

    Event save(Event event);

    List<Event> findByExternalIdIn(Collection<String> externalIds);

    List<Event> findByCarryDateAfterOrCarryDateNull(Instant after);

    List<Event> findByTypeAndCarryDateAfter(String type, Instant after);

    List<Event> findByTypeAndCarryDateNull(String type);
}
