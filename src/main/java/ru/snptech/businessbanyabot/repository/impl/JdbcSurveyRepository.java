package ru.snptech.businessbanyabot.repository.impl;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;
import ru.snptech.businessbanyabot.repository.SurveyRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JdbcSurveyRepository extends SurveyRepository, CrudRepository<Survey, Long> {

    @Override
    List<Survey> findByUserChatIdAndStatus(Long chatId, SurveyStatus status);

    @Override
    Optional<Survey> findFirstByUserChatIdOrderByCreatedAt(Long chatId);
}
