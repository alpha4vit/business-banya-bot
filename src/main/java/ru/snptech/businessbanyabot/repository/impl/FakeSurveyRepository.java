package ru.snptech.businessbanyabot.repository.impl;

import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.entity.SurveyQuestion;
import ru.snptech.businessbanyabot.repository.SurveyRepository;

import java.util.List;

@Component
public class FakeSurveyRepository implements SurveyRepository {

    @Override
    public List<SurveyQuestion> getQuestions() {
        return List.of(
            new SurveyQuestion(
                1L,
                "Введите ваше ФИО"
            ),
            new SurveyQuestion(
                2L,
                "Опишите сферу деятельности компании"
            ),
            new SurveyQuestion(
                3L,
                "Введите оборот вашей компании за год"
            ),
            new SurveyQuestion(
                4L,
                "Введите ваши интересы"
            )
        );
    }
}
