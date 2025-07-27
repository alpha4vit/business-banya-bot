package ru.snptech.businessbanyabot.integrations.bank.dto.request.webhook;

import java.util.List;

public record CreateWebhookRequest(
    List<String> webhooksList,
    String url
) {
}
