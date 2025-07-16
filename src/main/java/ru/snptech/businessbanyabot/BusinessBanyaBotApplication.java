package ru.snptech.businessbanyabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.snptech.businessbanyabot.integration.bitrix.client.BitrixIntegrationClient;

@EnableAsync
@EnableFeignClients(clients = {
        BitrixIntegrationClient.class
})
@SpringBootApplication
public class BusinessBanyaBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessBanyaBotApplication.class, args);
    }

}
