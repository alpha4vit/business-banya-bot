package ru.snptech.businessbanyabot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "survey")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(
        name = "chat_id",
        referencedColumnName = "chat_id"
    )
    private TelegramUser user;

    private String fio;

    private String companyTurnover;

    private String interests;

    private String activityScope;

    @Enumerated(value = EnumType.STRING)
    private SurveyStatus status;

    @CreationTimestamp
    private Instant createdAt;

    private Instant deliveredAt;
}
