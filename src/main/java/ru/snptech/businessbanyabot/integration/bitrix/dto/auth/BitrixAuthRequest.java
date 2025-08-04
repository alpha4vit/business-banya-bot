package ru.snptech.businessbanyabot.integration.bitrix.dto.auth;

import feign.form.FormProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BitrixAuthRequest {
    @FormProperty("grant_type")
    private String grantType;

    @FormProperty("client_id")
    private String clientId;

    @FormProperty("client_secret")
    private String clientSecret;

    @FormProperty("refresh_token")
    private String refreshToken;
}
