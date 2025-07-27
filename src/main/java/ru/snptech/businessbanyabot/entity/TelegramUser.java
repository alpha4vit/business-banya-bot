package ru.snptech.businessbanyabot.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.model.user.UserStatus;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "telegram_users")
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    private String telegramUsername;

    private String telegramFirstName;

    private String telegramLastName;

    private String fullName;

    private String phoneNumber;

    // base64 qrCode
    private String qrCode;

    @CreationTimestamp
    private Instant createdAt;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private String context;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private Instant bannedAt;

}
