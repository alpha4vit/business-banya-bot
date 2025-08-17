package ru.snptech.businessbanyabot.model.scenario.step;

import ru.snptech.businessbanyabot.model.scenario.ScenarioStep;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;

public enum EventScenarioStep implements ScenarioStep {

    INIT,
    EVENT_TYPE_CHOOSE,
    EVENT_SLIDER;

    @Override
    public ScenarioType type() {
        return ScenarioType.SEARCH;
    }
}
