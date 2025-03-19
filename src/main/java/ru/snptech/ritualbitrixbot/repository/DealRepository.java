package ru.snptech.ritualbitrixbot.repository;

import org.springframework.data.repository.CrudRepository;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.snptech.ritualbitrixbot.entity.Deal;
import ru.snptech.ritualbitrixbot.entity.DealStatus;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;

import java.util.Collection;
import java.util.List;

public interface DealRepository extends CrudRepository<Deal, String> {
    Deal findDealById(String id);

    long countAllByTelegramUser(TelegramUser telegramUser);

    List<Deal> findAllByTelegramUserAndStatusIn(TelegramUser telegramUser, Collection<DealStatus> statuses);

    List<Deal> findAllByTelegramUserAndStatusIsNull(TelegramUser telegramUser);

    List<Deal> findAllByTelegramUser(TelegramUser telegramUser);
}
