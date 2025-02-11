package ru.snptech.ritualbitrixbot.repository;


import org.springframework.data.repository.CrudRepository;
import ru.snptech.ritualbitrixbot.entity.Region;
import ru.snptech.ritualbitrixbot.entity.Role;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;

import java.util.List;

public interface TelegramUserRepository extends CrudRepository<TelegramUser, Long> {
    TelegramUser findByChatId(Long chatId);

    List<TelegramUser> findAllByRole(Role role);
    List<TelegramUser> findAllByRegion(Region region);
}
