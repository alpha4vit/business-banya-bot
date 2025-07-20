package ru.snptech.businessbanyabot.model.user;

import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

public record UserDto(
    Long chatId,

    String telegramUsername,

    String telegramFirstName,

    String telegramLastName,

    String fullName,

    String phoneNumber,

    @CreationTimestamp
    Instant createdAt,

    String context,

    Role role
) {
}
