package ru.snptech.businessbanyabot.propterties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private Integer leaderboardSize;
    private Deposit deposit;
    private Payment payment;
    private String personalDataConsentLink;
    private Integer subscriptionContinuationDurationInMonths;

    @Getter
    @Setter
    public static class Payment {
        private String currency;
        private Integer amount;
    }

    @Getter
    @Setter
    public static class Deposit {
        private Integer multiplicity;
        private String legalEntityLink;
    }
}
