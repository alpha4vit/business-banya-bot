package ru.snptech.businessbanyabot.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixCompanyDto;
import ru.snptech.businessbanyabot.model.user.UserRole;
import ru.snptech.businessbanyabot.model.user.UserStatus;

import java.time.Instant;

@Data
@Entity
@Table(name = "telegram_user")
public class TelegramUser {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    private String telegramUsername;

    private String telegramFirstName;

    private String telegramLastName;

    private String fullName;

    private String socialMedia;

    private String phoneNumber;

    @CreationTimestamp
    private Instant createdAt;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private String context;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "external_id")
    private String externalId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_info_id", referencedColumnName = "internal_id")
    private UserInfo info;

    private Instant bannedAt;

}
