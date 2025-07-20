package ru.snptech.businessbanyabot.repository.impl;

import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.SurveyQuestion;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.exception.BusinessBanyaInternalException;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;
import ru.snptech.businessbanyabot.repository.SurveyQuestionRepository;

import java.util.List;
import java.util.function.Predicate;

@Component
public class FakeSurveyQuestionRepository implements SurveyQuestionRepository {

    private final List<SurveyQuestion> questions = List.of(
        new SurveyQuestion(
            1L,
            "Введите ваше ФИО",
            SurveyScenarioStep.FIO
        ),
        new SurveyQuestion(
            2L,
            "Опишите сферу деятельности компании",
            SurveyScenarioStep.ACTIVITY_SCOPE
        ),
        new SurveyQuestion(
            3L,
            "Введите оборот вашей компании за год",
            SurveyScenarioStep.COMPANY_TURNOVER
        ),
        new SurveyQuestion(
            4L,
            "Введите ваши интересы",
            SurveyScenarioStep.INTERESTS
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
            .orElseThrow(() -> new BusinessBanyaDomainLogicException.SURVEY_NOT_FOUND());
    }
}
