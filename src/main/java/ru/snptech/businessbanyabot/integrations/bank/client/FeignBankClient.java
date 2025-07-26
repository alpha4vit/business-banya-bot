package ru.snptech.businessbanyabot.integrations.bank.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.RegisterPaymentQrCodeRequest;
import ru.snptech.businessbanyabot.integrations.bank.dto.response.RegisterPaymentQrCodeResponse;

@FeignClient(
    name = "bank-tochka-feign-client",
    url = "https://enter.tochka.com/sandbox/v2"
)
public interface FeignBankClient {

    @PostMapping("/sbp/{apiVersion}/qr-code/merchant/{merchantId}/{accountId}")
    ResponseEntity<RegisterPaymentQrCodeResponse> registerQrCode(
        @PathVariable("apiVersion") String apiVersion,
        @PathVariable("merchantId") String merchantId,
        @PathVariable("accountId") String accountId,
        @RequestBody RegisterPaymentQrCodeRequest registerPaymentQrCodeRequest
    );

    @PostMapping("/webhook/{apiVersion}/{client_id}")
    ResponseEntity<String> createWebhook(
        @PathVariable("apiVersion") String apiVersion,
        @PathVariable("clientId") String clientId,
        @RequestBody RegisterPay mentQrCodeRequest registerPaymentQrCodeRequest
    ) {

    }
}
