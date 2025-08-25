package ru.snptech.businessbanyabot.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.snptech.businessbanyabot.entity.ButtonStatsEntity;
import ru.snptech.businessbanyabot.repository.ButtonStatsRepository;

@Repository
public interface JdbcButtonStatsRepository extends ButtonStatsRepository, JpaRepository<ButtonStatsEntity, String> {
}
