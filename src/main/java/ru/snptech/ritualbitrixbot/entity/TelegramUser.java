package ru.snptech.ritualbitrixbot.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "telegram_users")
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
    private boolean active = false;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;
}
