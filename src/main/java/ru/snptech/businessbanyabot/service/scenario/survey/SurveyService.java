package ru.snptech.businessbanyabot.service.scenario.survey;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.exception.BusinessBanyaDomainLogicException;
import ru.snptech.businessbanyabot.model.survey.SurveyStatus;
import ru.snptech.businessbanyabot.repository.SurveyRepository;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    @Transactional
    public Survey applyStatus(Long id, SurveyStatus status) {
        var survey = surveyRepository.findById(id)
            .orElseThrow(BusinessBanyaDomainLogicException.SURVEY_NOT_FOUND::new);

        survey.setStatus(status);

        return surveyRepository.save(survey);
    }
}
