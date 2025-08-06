package ru.snptech.businessbanyabot.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.snptech.businessbanyabot.entity.TelegramUser;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserRepository {

    TelegramUser save(TelegramUser user);

    <S extends TelegramUser> Iterable<S> saveAll(Iterable<S> users);

    TelegramUser findByChatId(Long chatId);

    List<TelegramUser> findByChatIdIn(Set<Long> chatIds);

    List<TelegramUser> findByFullNameContainingIgnoreCase(String partOfName);

    List<TelegramUser> findByExternalIdIn(Collection<String> externalIds);

    List<TelegramUser> findByPhoneNumberIn(Collection<String> phoneNumbers);

    List<TelegramUser> findByFullNameIn(Collection<String> fullNames);

    List<TelegramUser> findAll(Specification<TelegramUser> specification);
}
