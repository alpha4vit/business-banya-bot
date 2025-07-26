package ru.snptech.businessbanyabot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;

import java.time.Instant;
import java.time.LocalDate;

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

    @Column(name = "fio")
    private String fio;

    @Column(name = "social_media")
    private String socialMedia;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "city")
    private String city;

    @Column(name = "years_of_experience")
    private String yearsOfExperience;

    @Column(name = "business_description")
    private String businessDescription;

    @Column(name = "withdrawals")
    private String withdrawals;

    @Column(name = "family_status")
    private String familyStatus;

    @Column(name = "children_count")
    private String childrenCount;

    @Column(name = "sport_interests")
    private String sportInterests;

    @Column(name = "beliefs")
    private String beliefs;

    @Column(name = "music_singers")
    private String musicSingers;

    @Column(name = "crucial_words")
    private String crucialWords;

    @Column(name = "films")
    private String films;

    @Column(name = "strengths")
    private String strengths;

    @Column(name = "victories")
    private String victories;

    @Column(name = "defeats")
    private String defeats;

    @Column(name = "teachers")
    private String teachers;

    @Column(name = "goals")
    private String goals;

    @Column(name = "active_side")
    private String activeSide;

    @Column(name = "passive_side")
    private String passiveSide;

    @Column(name = "referrer")
    private String referrer;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private SurveyStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;


}
