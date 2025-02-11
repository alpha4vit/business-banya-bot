package ru.snptech.ritualbitrixbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.snptech.ritualbitrixbot.integration.bitrix.client.BitrixIntegrationClient;

@EnableAsync
@EnableFeignClients(clients = {
        BitrixIntegrationClient.class
})
@SpringBootApplication
public class RitualBitrixBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(RitualBitrixBotApplication.class, args);
    }

}
