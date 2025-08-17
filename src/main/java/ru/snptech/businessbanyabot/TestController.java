package ru.snptech.businessbanyabot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import ru.snptech.businessbanyabot.integration.bank.dto.request.webhook.IncomingSbpPaymentDto;
import ru.snptech.businessbanyabot.integration.bank.properties.BankIntegrationProperties;
import ru.snptech.businessbanyabot.integration.bank.service.BankIntegrationService;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixAuthService;
import ru.snptech.businessbanyabot.service.payment.PaymentService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final ObjectMapper objectMapper;
    private final BitrixAuthService bitrixAuthService;
    private final PaymentService paymentService;

    @GetMapping
    public String test() {

        bitrixAuthService.getValidAccessToken();

        return "code";
    }

    @SneakyThrows
    @PostMapping("/post")
    public void test(@RequestBody String body) {
        var jwtData = SignedJWT.parse(body);

        var webhookParsedData = jwtData.getJWTClaimsSet().getClaims();

        var incomingPayment = objectMapper.convertValue(
            webhookParsedData,
            new TypeReference<IncomingSbpPaymentDto>() {}
        );

        paymentService.applyFastPayment(incomingPayment.qrcId());
    }

    @PutMapping("/put")
    public String testPut(@RequestBody String body) {
        log.error("PUTPUTPUTPUT");
        log.error(body);

        return body;
    }

}
