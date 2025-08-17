package ru.snptech.businessbanyabot.integration.bank.dto.request.webhook;

public record IncomingSbpPaymentDto(
        String operationId,
        String qrcId,
        String amount,
        String payerMobileNumber,
        String payerName,
        String brandName,
        String merchantId,
        String purpose,
        String webhookType,
        String customerCode,
        String refTransactionId
) {}
