package ru.snptech.businessbanyabot.model.scenario.step;

import ru.snptech.businessbanyabot.model.scenario.ScenarioStep;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;

public enum SurveyScenarioStep implements ScenarioStep {

    INITIAL,

    FIO,
    SOCIAL_MEDIA,
    DATE_OF_BIRTH,
    CITY,
    YEARS_OF_EXPERIENCE,
    BUSINESS_DESCRIPTION,
    WITHDRAWALS,
    FAMILY_STATUS,
    CHILDREN_COUNT,
    SPORT_INTERESTS,
    BELIEFS,
    MUSIC_SINGERS,
    CRUCIAL_WORDS,
    FILMS,
    STRENGTHS,
    VICTORIES,
    DEFEATS,
    TEACHERS,
    GOALS,
    ACTIVE_SIDE,
    PASSIVE_SIDE,
    REFERRER;

    @Override
    public ScenarioType type() {
        return ScenarioType.SURVEY;
    }
}
