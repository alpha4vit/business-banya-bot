package ru.snptech.businessbanyabot.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.snptech.businessbanyabot.entity.ButtonStatsEntity;
import ru.snptech.businessbanyabot.entity.Payment;

import java.util.List;

public interface ButtonStatsRepository {

    ButtonStatsEntity save(ButtonStatsEntity user);

    @Modifying
    @Query("update ButtonStatsEntity b set b.count = b.count + 1 where b.buttonName = :buttonName")
    void increment(@Param("buttonName") String buttonName);

    List<ButtonStatsEntity> findAll();
}
