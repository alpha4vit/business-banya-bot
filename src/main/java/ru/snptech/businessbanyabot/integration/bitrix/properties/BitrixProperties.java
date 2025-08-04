package ru.snptech.businessbanyabot.integration.bitrix.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.integration.bitrix")
public class BitrixProperties {

    private String url;
    private String clientId;
    private String clientSecret;
    private String accessToken;
    private String refreshToken;
}
