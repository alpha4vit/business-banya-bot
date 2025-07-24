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

    public Survey updateField(SurveyScenarioStep step, String value) {
        switch (step) {
            case FIO -> setFio(value);
            case SOCIAL_MEDIA -> setSocialMedia(value);
            case DATE_OF_BIRTH -> setDateOfBirth(LocalDate.parse(value));
            case CITY -> setCity(value);
            case YEARS_OF_EXPERIENCE -> setYearsOfExperience(value);
            case BUSINESS_DESCRIPTION -> setBusinessDescription(value);
            case WITHDRAWALS -> setWithdrawals(value);
            case FAMILY_STATUS -> setFamilyStatus(value);
            case CHILDREN_COUNT -> setChildrenCount(value);
            case SPORT_INTERESTS -> setSportInterests(value);
            case BELIEFS -> setBeliefs(value);
            case MUSIC_SINGERS -> setMusicSingers(value);
            case CRUCIAL_WORDS -> setCrucialWords(value);
            case FILMS -> setFilms(value);
            case STRENGTHS -> setStrengths(value);
            case VICTORIES -> setVictories(value);
            case DEFEATS -> setDefeats(value);
            case TEACHERS -> setTeachers(value);
            case GOALS -> setGoals(value);
            case ACTIVE_SIDE -> setActiveSide(value);
            case PASSIVE_SIDE -> setPassiveSide(value);
            case REFERRER -> setReferrer(value);
        }

        return this;
    }
}
