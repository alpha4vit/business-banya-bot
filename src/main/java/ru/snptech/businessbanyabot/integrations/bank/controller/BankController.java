package ru.snptech.businessbanyabot.integrations.bank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RequestMapping("${application.integration.bank.webhook.url}")
@RequiredArgsConstructor
public class BankController {

    @GetMapping
    public void getWebhookUpdate(String value) {
        log.info("New bank webhook update - {}", value);
    }
}
