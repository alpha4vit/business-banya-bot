package ru.snptech.businessbanyabot.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.snptech.businessbanyabot.entity.Event;
import ru.snptech.businessbanyabot.repository.EventRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface JdbcEventRepository extends EventRepository, JpaRepository<Event, UUID> {

    List<Event> findByCarryDateAfterOrCarryDateNull(Instant after);
}
