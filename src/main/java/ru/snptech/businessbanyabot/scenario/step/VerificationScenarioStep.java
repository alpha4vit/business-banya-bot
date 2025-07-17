package ru.snptech.businessbanyabot.scenario.step;

import ru.snptech.businessbanyabot.scenario.ScenarioStep;
import ru.snptech.businessbanyabot.scenario.ScenarioType;

public class VerificationScenarioStep implements ScenarioStep {

    @Override
    public ScenarioType type() {
        return ScenarioType.VERIFICATION;
    }

    public static String AGREEMENT_SENT = "AGREEMENT_SENT";
    public static String WAITING_NUMBER = "WAITING_NUMBER";
}
