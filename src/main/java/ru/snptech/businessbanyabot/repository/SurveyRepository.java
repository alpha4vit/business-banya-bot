package ru.snptech.businessbanyabot.repository;

import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository {

    List<Survey> findByUserChatIdAndStatus(Long chatId, SurveyStatus status);

    Survey save(Survey survey);

    Optional<Survey> findFirstByUserChatIdOrderByCreatedAt(Long chatId);

    Optional<Survey> findById(Long id);

}
