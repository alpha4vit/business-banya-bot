package ru.snptech.businessbanyabot.model.scenario.step;

import ru.snptech.businessbanyabot.model.scenario.ScenarioStep;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;

public enum SearchScenarioStep implements ScenarioStep {

    INIT,
    SEARCH_INPUT,
    RESIDENT_SLIDER;

    @Override
    public ScenarioType type() {
        return ScenarioType.SEARCH;
    }
}
