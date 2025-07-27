package ru.snptech.businessbanyabot.integrations.bank.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.qr.RegisterPaymentQrCodeRequest;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.webhook.CreateWebhookRequest;
import ru.snptech.businessbanyabot.integrations.bank.dto.response.qr.RegisterPaymentQrCodeResponse;

@FeignClient(
    name = "bank-tochka-feign-client",
    url = "https://enter.tochka.com/uapi/",
    configuration = FeignBankClientConfiguration.class
)
public interface FeignBankClient {

    @PostMapping("/sbp/{apiVersion}/qr-code/merchant/{merchantId}/{accountId}")
    ResponseEntity<RegisterPaymentQrCodeResponse> registerQrCode(
        @PathVariable("apiVersion") String apiVersion,
        @PathVariable("merchantId") String merchantId,
        @PathVariable("accountId") String accountId,
        @RequestBody RegisterPaymentQrCodeRequest registerPaymentQrCodeData
    );

    @PutMapping("/webhook/{apiVersion}/{clientId}")
    ResponseEntity<String> createWebhook(
        @PathVariable("apiVersion") String apiVersion,
        @PathVariable("clientId") String clientId,
        @RequestBody CreateWebhookRequest createWebhookRequest
    );
}
