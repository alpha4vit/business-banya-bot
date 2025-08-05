package ru.snptech.businessbanyabot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.snptech.businessbanyabot.entity.Event;
import ru.snptech.businessbanyabot.integration.bank.dto.common.WeekDay;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface JdbcEventRepository extends EventRepository, JpaRepository<Event, UUID> {
}
