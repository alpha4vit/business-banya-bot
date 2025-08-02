package ru.snptech.businessbanyabot.repository.impl;


import org.springframework.data.repository.CrudRepository;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.repository.UserRepository;

import java.util.Collection;
import java.util.List;

public interface JdbcUserRepository extends CrudRepository<TelegramUser, Long>, UserRepository {

    @Override
    TelegramUser findByChatId(Long chatId);

    @Override
    List<TelegramUser> findByFullNameContainingIgnoreCase(String partOfName);

    @Override
    List<TelegramUser> findByExternalIdIn(Collection<String> externalIds);

    @Override
    List<TelegramUser> findByPhoneNumberIn(Collection<String> phoneNumbers);
}
