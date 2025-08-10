package ru.snptech.businessbanyabot.repository;

import ru.snptech.businessbanyabot.entity.UserInfo;

public interface UserInfoRepository {

    UserInfo save(UserInfo userInfo);

}
