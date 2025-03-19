package ru.snptech.ritualbitrixbot.repository;


import org.springframework.data.repository.CrudRepository;
import ru.snptech.ritualbitrixbot.entity.Region;
import ru.snptech.ritualbitrixbot.entity.Role;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;

import java.util.List;
import java.util.Map;

public interface TelegramUserRepository extends CrudRepository<TelegramUser, Long> {
    TelegramUser findByChatId(Long chatId);

    List<TelegramUser> findAllByRole(Role role);
    List<TelegramUser> findAllByRegion(Region region);
    List<TelegramUser> findAllByRegion_Id(Long region_id);
    List<TelegramUser> findAllByChatIdOrPartnerAccount_ChatId(Long chatId, Long partnerAccountId);

    TelegramUser findByTelegramUsername(String telegramUsername);
    List<TelegramUser> findAllByRoleAndActiveTrue(Role role);

    List<TelegramUser> findAllByPartnerAccount_ChatId(Long partnerAccountChatId);
}
