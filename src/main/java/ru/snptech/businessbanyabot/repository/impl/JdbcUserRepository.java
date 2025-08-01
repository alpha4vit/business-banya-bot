package ru.snptech.businessbanyabot.repository.impl;


import org.springframework.data.repository.CrudRepository;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.repository.UserRepository;

import java.util.List;

public interface JdbcUserRepository extends CrudRepository<TelegramUser, Long>, UserRepository {

    @Override
    TelegramUser findByChatId(Long chatId);

    @Override
    List<TelegramUser> findByFullNameContainingIgnoreCase(String partOfName);

}
