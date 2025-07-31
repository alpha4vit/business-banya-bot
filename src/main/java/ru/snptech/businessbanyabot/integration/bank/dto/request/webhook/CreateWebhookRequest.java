package ru.snptech.businessbanyabot.integration.bank.dto.request.webhook;

import java.util.List;

public record CreateWebhookRequest(
    List<String> webhooksList,
    String url
) {
}
