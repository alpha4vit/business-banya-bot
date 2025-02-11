package ru.snptech.ritualbitrixbot.entity;

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

@Getter
@Setter
@Entity
@Table(name = "deals")
public class Deal {
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private DealFlow flow;
    @Enumerated(EnumType.STRING)
    private DealStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "telegram_user_chat_id")
    private TelegramUser telegramUser;
    private String source;
    private String source2;
    private String dealType;
    @Column(length = 2000)
    private String comment;
    private String contactPhone;
    private String contactName;
    private String requestedPhone;
    private String address;
    private String city;
    private String region;
    private String customerName;
    private String deceasedSurname;
}
