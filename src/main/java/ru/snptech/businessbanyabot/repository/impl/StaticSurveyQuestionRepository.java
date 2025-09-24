package ru.snptech.businessbanyabot.repository.impl;

import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.SurveyQuestion;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;
import ru.snptech.businessbanyabot.repository.SurveyQuestionRepository;

import java.util.List;
import java.util.function.Predicate;

@Component
public class StaticSurveyQuestionRepository implements SurveyQuestionRepository {

    private final List<SurveyQuestion> questions = List.of(
        new SurveyQuestion(
            1L,
            "Укажите ваше Имя и Фамилию на русском языке",
            SurveyScenarioStep.FIO
        )
    );

    @Override
    public List<SurveyQuestion> getQuestions() {
        return questions;
    }

    @Override
    public SurveyQuestion getByScenarioStep(final SurveyScenarioStep step) {
        return getByPredicate((it) -> it.getScenarioStep().equals(step));
    }

    @Override
    public SurveyQuestion getById(final Long id) {
        return getByPredicate((it) -> it.getNumber().equals(id));
    }

    private SurveyQuestion getByPredicate(final Predicate<SurveyQuestion> predicate) {
        return questions.stream()
            .filter(predicate)
            .findFirst()
            .orElseThrow(BusinessBanyaInternalException.SURVEY_QUESTION_NOT_FOUND::new);
    }
}
