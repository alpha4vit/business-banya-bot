package ru.snptech.businessbanyabot.integration.bank.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.integration.bank")
public class BankIntegrationProperties {
    private String url;
    private String merchantId;
    private String clientId;
    private String accountId;
    private String apiVersion;
    private String token;
    private Webhook webhook;
    private QrCodeSettings qrCodeSettings;

    @Getter
    @Setter
    public static class Webhook {
        private String url;
    }

    @Getter
    @Setter
    public static class QrCodeSettings {
        private Duration ttl;
        private QrCodeImageSettings imageSettings;
    }

    @Getter
    @Setter
    public static class QrCodeImageSettings {
        private Integer size;
        private String mediaType;
    }
}
