package ru.snptech.businessbanyabot.integration.bank.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.snptech.businessbanyabot.integration.bank.dto.request.webhook.IncomingSbpPaymentDto;
import ru.snptech.businessbanyabot.integration.bank.properties.BankIntegrationProperties;
import ru.snptech.businessbanyabot.service.payment.PaymentService;


@Slf4j
@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {

    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;

    @SneakyThrows
    @PostMapping("/payment/incoming")
    public void getWebhookUpdate(@RequestBody String value) {
        var jwtData = SignedJWT.parse(value);

        var webhookParsedData = jwtData.getJWTClaimsSet().getClaims();

        var incomingPayment = objectMapper.convertValue(
            webhookParsedData,
            new TypeReference<IncomingSbpPaymentDto>() {}
        );

        paymentService.applyFastPayment(incomingPayment.qrcId());
    }
}
