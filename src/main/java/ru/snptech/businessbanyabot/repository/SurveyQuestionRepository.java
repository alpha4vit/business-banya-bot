package ru.snptech.businessbanyabot.repository;

import ru.snptech.businessbanyabot.entity.SurveyQuestion;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;

import java.util.List;

public interface SurveyQuestionRepository {

    List<SurveyQuestion> getQuestions();

    SurveyQuestion getByScenarioStep(SurveyScenarioStep step);

    SurveyQuestion getById(Long id);
}
