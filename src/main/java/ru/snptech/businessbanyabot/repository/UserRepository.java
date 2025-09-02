package ru.snptech.businessbanyabot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.snptech.businessbanyabot.entity.TelegramUser;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    TelegramUser save(TelegramUser user);

    <S extends TelegramUser> Iterable<S> saveAll(Iterable<S> users);

    TelegramUser findByChatId(Long chatId);

    Optional<TelegramUser> findByExternalId(String externalId);

    List<TelegramUser> findByChatIdIn(Set<Long> chatIds);

    List<TelegramUser> findByFullNameContainingIgnoreCase(String partOfName);
    List<TelegramUser> findByPhoneNumberContainingIgnoreCase(String phoneNumber);

    List<TelegramUser> findByPhoneNumberIn(Collection<String> phoneNumbers);

    List<TelegramUser> findAll(Specification<TelegramUser> specification);

    List<TelegramUser> findAll(Specification<TelegramUser> specification, Pageable pageable);

    List<TelegramUser> findAll();

    List<TelegramUser> findAllByResidentUntilBetween(Instant from, Instant to);


}
