package ru.snptech.businessbanyabot.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.snptech.businessbanyabot.entity.UserInfo;
import ru.snptech.businessbanyabot.repository.UserInfoRepository;

@Repository
public interface JdbcUserInfoRepository extends JpaRepository<UserInfo, Long>, UserInfoRepository {
}
