package ru.snptech.businessbanyabot.model.scenario.step;

import ru.snptech.businessbanyabot.model.scenario.ScenarioStep;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;

public enum SurveyScenarioStep implements ScenarioStep {
    INITIAL,
    FIO,
    ACTIVITY_SCOPE,
    COMPANY_TURNOVER,
    INTERESTS;

    @Override
    public ScenarioType type() {
        return ScenarioType.SURVEY;
    }
}
