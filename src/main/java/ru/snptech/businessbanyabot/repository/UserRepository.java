package ru.snptech.businessbanyabot.repository;

import ru.snptech.businessbanyabot.entity.TelegramUser;

public interface UserRepository {

    TelegramUser save(TelegramUser user);

    TelegramUser findByChatId(Long chatId);

}
