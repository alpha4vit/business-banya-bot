package ru.snptech.businessbanyabot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;

@Getter
@Setter
@Entity
@Table(name = "survey_question")
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    private String message;

    @Enumerated(value = EnumType.STRING)
    private SurveyScenarioStep scenarioStep;
}
