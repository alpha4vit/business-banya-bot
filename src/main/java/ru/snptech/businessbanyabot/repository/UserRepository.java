package ru.snptech.businessbanyabot.repository;

import ru.snptech.businessbanyabot.entity.TelegramUser;

import java.util.List;

public interface UserRepository {

    TelegramUser save(TelegramUser user);

    TelegramUser findByChatId(Long chatId);

    List<TelegramUser> findByFullNameContainingIgnoreCase(String partOfName);

}
