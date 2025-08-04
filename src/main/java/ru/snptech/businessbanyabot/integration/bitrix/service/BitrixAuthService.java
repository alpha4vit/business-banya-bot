package ru.snptech.businessbanyabot.integration.bitrix.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.snptech.businessbanyabot.integration.bitrix.client.BitrixAuthClient;
import ru.snptech.businessbanyabot.integration.bitrix.properties.BitrixProperties;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class BitrixAuthService {

    private static final String REFRESH_GRANT_TYPE = "refresh_token";

    private final BitrixAuthClient bitrixAuthClient;
    private final BitrixProperties bitrixProperties;

    private String accessToken = "";
    private String refreshToken = "";

    @PostConstruct
    public void init() {
        accessToken = bitrixProperties.getAccessToken();
        refreshToken = bitrixProperties.getRefreshToken();
    }


    private Instant expiresAt = Instant.now();

    public synchronized String getValidAccessToken() {
        if (Instant.now().isAfter(expiresAt)) {
            log.info("Access token expired. Refreshing...");

            refreshAccessToken();
        }

        return accessToken;
    }

    private void refreshAccessToken() {
        var response = bitrixAuthClient.refreshToken(
            REFRESH_GRANT_TYPE,
            bitrixProperties.getClientId(),
            bitrixProperties.getClientSecret(),
            refreshToken
        );

        accessToken = response.accessToken();
        refreshToken = response.refreshToken();
        expiresAt = response.expiresAt();

        log.info("Access token refreshed successfully.");
    }
}

