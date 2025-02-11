package ru.snptech.ritualbitrixbot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.snptech.ritualbitrixbot.entity.Deal;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;

public interface DealRepository extends CrudRepository<Deal, String> {
    Deal findDealById(String id);

    long countAllByTelegramUser(TelegramUser telegramUser);
}
