package ru.snptech.ritualbitrixbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.snptech.ritualbitrixbot.entity.TelegramUser;
import ru.snptech.ritualbitrixbot.repository.TelegramUserRepository;
import ru.snptech.ritualbitrixbot.types.MetaDataKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserContextService {
    private final TelegramUserRepository telegramUserRepository;
    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserContext(TelegramUser telegramUser) {
        return Optional
                .ofNullable(telegramUser.getContext())
                .map(stringContext -> {
                    try {
                        return objectMapper.readValue(stringContext, Map.class);
                    } catch (JsonProcessingException e) {
                        return new HashMap<String, Object>();
                    }
                })
                .orElse(new HashMap<String, Object>());
    }

    @SneakyThrows
    public void updateUserContext(TelegramUser telegramUser, Map<String, Object> userContext) {
        telegramUser.setContext(objectMapper.writeValueAsString(userContext));
        telegramUser = telegramUserRepository.save(telegramUser);
    }

    public <T> void updateUserContext(TelegramUser telegramUser, MetaDataKey<T> metaDataKey, T value) {
        var context = getUserContext(telegramUser);
        metaDataKey.setValue(context, value);
        updateUserContext(telegramUser, context);
    }

    public <T> T getUserContextParamValue(TelegramUser telegramUser, MetaDataKey<T> metaDataKey) {
        return metaDataKey.getValue(getUserContext(telegramUser));
    }

    public void cleanUserContext(TelegramUser telegramUser) {
        updateUserContext(telegramUser, Collections.emptyMap());
    }

}
