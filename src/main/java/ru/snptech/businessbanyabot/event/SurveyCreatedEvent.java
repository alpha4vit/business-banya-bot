package ru.snptech.businessbanyabot.event;

import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.entity.TelegramUser;

public record SurveyCreatedEvent(
    TelegramUser user,
    Survey survey
) {
}
