package ru.snptech.businessbanyabot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.snptech.businessbanyabot.entity.TelegramUser;
import ru.snptech.businessbanyabot.entity.UserInfo;

public interface UserInfoRepository {

    UserInfo save(UserInfo userInfo);

    <S extends UserInfo> Iterable<S> saveAll(Iterable<S> info);

    @Query(
        value = """
            SELECT rank
            FROM (
                     SELECT internal_id,
                            RANK() OVER (
                                ORDER BY COALESCE(NULLIF(points, '')::int, 0) DESC
                                ) AS rank
                     FROM user_info
                 ) ranked
            WHERE internal_id = :chatId and rank != 0
            """,
        nativeQuery = true
    )
    Integer getUserRank(@Param("chatId") Long chatId);

}
