package ru.snptech.businessbanyabot.model.scenario.step;

import ru.snptech.businessbanyabot.model.scenario.ScenarioStep;
import ru.snptech.businessbanyabot.model.scenario.ScenarioType;

public class VerificationScenarioStep implements ScenarioStep {

    @Override
    public ScenarioType type() {
        return ScenarioType.VERIFICATION;
    }

    public static String AGREEMENT_SENT = "AGREEMENT_SENT";
    public static String WAITING_NUMBER = "WAITING_NUMBER";
}
