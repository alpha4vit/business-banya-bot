package ru.snptech.businessbanyabot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.snptech.businessbanyabot.entity.Deal;
import ru.snptech.businessbanyabot.entity.DealStatus;
import ru.snptech.businessbanyabot.entity.TelegramUser;

import java.util.Collection;
import java.util.List;

public interface DealRepository extends CrudRepository<Deal, String> {
    Deal findDealById(String id);

    long countAllByTelegramUser(TelegramUser telegramUser);

    List<Deal> findAllByTelegramUserAndStatusIn(TelegramUser telegramUser, Collection<DealStatus> statuses);

    List<Deal> findAllByTelegramUserAndStatusIsNull(TelegramUser telegramUser);

    List<Deal> findAllByTelegramUser(TelegramUser telegramUser);
}
