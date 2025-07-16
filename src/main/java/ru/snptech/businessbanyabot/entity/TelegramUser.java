package ru.snptech.businessbanyabot.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "telegram_users")
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser {
    @Id
    private Long chatId;

    private String telegramUsername;

    private String telegramFirstName;

    private String telegramLastName;

    private String fullName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private String context;

    @Enumerated(EnumType.STRING)
    private Role role;

}
