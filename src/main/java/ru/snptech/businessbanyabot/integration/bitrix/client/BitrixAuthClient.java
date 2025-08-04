package ru.snptech.businessbanyabot.integration.bitrix.client;

import feign.form.FormProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.snptech.businessbanyabot.integration.bitrix.dto.auth.BitrixAuthRequest;
import ru.snptech.businessbanyabot.integration.bitrix.dto.auth.BitrixAuthResponse;

@FeignClient(
    name = "bitrix-auth-feign-client",
    url = "${application.integration.bitrix.auth-url}",
    configuration = FeignBitrixClientConfiguration.class
)
public interface BitrixAuthClient {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    BitrixAuthResponse refreshToken(
        @RequestParam("grant_type") String grantType,
        @RequestParam("client_id") String clientId,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("refresh_token") String refreshToken
    );
}
