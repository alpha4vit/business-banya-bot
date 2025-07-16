package ru.snptech.businessbanyabot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.snptech.businessbanyabot.entity.LinkedMessageEntity;

import java.util.List;

public interface LinkedMessagesRepository extends CrudRepository<LinkedMessageEntity, Long> {
    List<LinkedMessageEntity> findByDeal_IdAndTelegramUser_ChatId(String dealId, Long telegramUserChatId);
}
