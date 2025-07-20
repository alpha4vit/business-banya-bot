package ru.snptech.businessbanyabot.repository.impl;


import org.springframework.data.repository.CrudRepository;
import ru.snptech.businessbanyabot.model.user.Role;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.repository.UserRepository;

import java.util.List;

public interface JdbcUserRepository extends CrudRepository<TelegramUser, Long>, UserRepository {
    TelegramUser findByChatId(Long chatId);

    List<TelegramUser> findAllByRole(Role role);

    TelegramUser findByTelegramUsername(String telegramUsername);
}
