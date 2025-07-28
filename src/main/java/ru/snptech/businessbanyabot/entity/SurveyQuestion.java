package ru.snptech.businessbanyabot.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "survey_question")
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    private String message;

    @Enumerated(value = EnumType.STRING)
    private SurveyScenarioStep scenarioStep;
}
