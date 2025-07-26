package ru.snptech.businessbanyabot.service.survey;

import ru.snptech.businessbanyabot.entity.Survey;
import ru.snptech.businessbanyabot.model.scenario.step.SurveyScenarioStep;

import java.time.LocalDate;

public class SurveyStepMappings {

    public static Survey updateField(Survey survey, SurveyScenarioStep step, String value) {
        switch (step) {
            case FIO -> survey.setFio(value);
            case SOCIAL_MEDIA -> survey.setSocialMedia(value);
            case DATE_OF_BIRTH -> survey.setDateOfBirth(LocalDate.parse(value));
            case CITY -> survey.setCity(value);
            case YEARS_OF_EXPERIENCE -> survey.setYearsOfExperience(value);
            case BUSINESS_DESCRIPTION -> survey.setBusinessDescription(value);
            case WITHDRAWALS -> survey.setWithdrawals(value);
            case FAMILY_STATUS -> survey.setFamilyStatus(value);
            case CHILDREN_COUNT -> survey.setChildrenCount(value);
            case SPORT_INTERESTS -> survey.setSportInterests(value);
            case BELIEFS -> survey.setBeliefs(value);
            case MUSIC_SINGERS -> survey.setMusicSingers(value);
            case CRUCIAL_WORDS -> survey.setCrucialWords(value);
            case FILMS -> survey.setFilms(value);
            case STRENGTHS -> survey.setStrengths(value);
            case VICTORIES -> survey.setVictories(value);
            case DEFEATS -> survey.setDefeats(value);
            case TEACHERS -> survey.setTeachers(value);
            case GOALS -> survey.setGoals(value);
            case ACTIVE_SIDE -> survey.setActiveSide(value);
            case PASSIVE_SIDE -> survey.setPassiveSide(value);
            case REFERRER -> survey.setReferrer(value);
        }

        return survey;
    }

}
