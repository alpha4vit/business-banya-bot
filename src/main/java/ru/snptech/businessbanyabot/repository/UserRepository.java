package ru.snptech.businessbanyabot.repository;


import org.springframework.data.repository.CrudRepository;
import ru.snptech.businessbanyabot.entity.Role;
import ru.snptech.businessbanyabot.entity.TelegramUser;

import java.util.List;

public interface UserRepository extends CrudRepository<TelegramUser, Long> {
    TelegramUser findByChatId(Long chatId);

    List<TelegramUser> findAllByRole(Role role);
    List<TelegramUser> findAllByRegion(Region region);
    List<TelegramUser> findAllByRegion_Id(Long region_id);
    List<TelegramUser> findAllByChatIdOrPartnerAccount_ChatId(Long chatId, Long partnerAccountId);

    TelegramUser findByTelegramUsername(String telegramUsername);
    List<TelegramUser> findAllByRoleAndActiveTrue(Role role);

    List<TelegramUser> findAllByPartnerAccount_ChatId(Long partnerAccountChatId);
}
