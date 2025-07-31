package ru.snptech.businessbanyabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.snptech.businessbanyabot.integration.bank.client.FeignBankClient;
import ru.snptech.businessbanyabot.integration.bitrix.client.BitrixCrmClient;

@EnableAsync
@EnableScheduling
@EnableFeignClients(clients = {
    FeignBankClient.class,
    BitrixCrmClient.class
})
@SpringBootApplication
public class BusinessBanyaBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessBanyaBotApplication.class, args);
    }

}
