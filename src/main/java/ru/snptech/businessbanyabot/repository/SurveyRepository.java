package ru.snptech.businessbanyabot.repository;

import ru.snptech.businessbanyabot.entity.SurveyQuestion;

import java.util.List;

public interface SurveyRepository {

    List<SurveyQuestion> getQuestions();

}
