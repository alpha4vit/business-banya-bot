package ru.snptech.businessbanyabot.repository;

import ru.snptech.businessbanyabot.entity.TelegramUser;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    TelegramUser save(TelegramUser user);

    <S extends TelegramUser> Iterable<S> saveAll(Iterable<S> users);

    TelegramUser findByChatId(Long chatId);

    List<TelegramUser> findByFullNameContainingIgnoreCase(String partOfName);

    List<TelegramUser> findByExternalIdIn(Collection<String> externalIds);

    List<TelegramUser> findByPhoneNumberIn(Collection<String> phoneNumbers);
}
