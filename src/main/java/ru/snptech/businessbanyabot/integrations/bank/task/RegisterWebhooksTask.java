package ru.snptech.businessbanyabot.integrations.bank.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.snptech.businessbanyabot.integrations.bank.dto.request.webhook.CreateWebhookRequest;
import ru.snptech.businessbanyabot.integrations.bank.properties.BankIntegrationProperties;
import ru.snptech.businessbanyabot.integrations.bank.service.BankIntegrationService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterWebhooksTask {

    private final BankIntegrationService bankIntegrationService;
    private final BankIntegrationProperties bankIntegrationProperties;


    @EventListener(ApplicationReadyEvent.class)
    public void registerWebhooks() {
        log.info("STARTING REGISTERING BANK WEBHOOKS");

        // TODO remove try - catch, application should be shut down, when registering webhooks failed
        try {
            bankIntegrationService.createWebhook(
                new CreateWebhookRequest(
                    List.of(
                        "incomingPayment"
                    ),
                    bankIntegrationProperties.getWebhook().getUrl()
                )
            );

            log.info("BANK WEBHOOKS SUCCESSFULLY REGISTERED");
        } catch (Throwable t) {
            log.error("BANK WEBHOOKS REGISTRATION FAILED '{}'", t.getMessage());
        }
    }
}
