package ru.snptech.businessbanyabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.snptech.businessbanyabot.integration.bank.client.FeignBankClient;
import ru.snptech.businessbanyabot.integration.bitrix.client.BitrixAuthClient;
import ru.snptech.businessbanyabot.integration.bitrix.client.BitrixCrmClient;
import ru.snptech.businessbanyabot.model.report.ReportType;
import ru.snptech.businessbanyabot.repository.UserRepository;
import ru.snptech.businessbanyabot.service.report.*;

import java.util.Map;

@EnableAsync
@EnableScheduling
@EnableFeignClients(clients = {
    FeignBankClient.class,
    BitrixCrmClient.class,
    BitrixAuthClient.class,
})
@SpringBootApplication
public class BusinessBanyaBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessBanyaBotApplication.class, args);
    }

    @Bean
    public ReportService reportService(
        UserRepository userRepository
    ) {
        return new DispatchingReportService(
            Map.of(
                ReportType.FAMILY_STATUS, new FamilyStatusReportService(userRepository),
                ReportType.CHILDREN_COUNT, new ChildrenCountReportService(userRepository),
                ReportType.BUSINESS_CLIENTS, new BusinessClientReportService(userRepository),
                ReportType.GROWTH_LIMIT, new GrowthLimitReportService(userRepository),
                ReportType.EMPLOYEE_COUNT, new EmployeeCountReportService(userRepository),
                ReportType.CITY, new CityReportService(userRepository)
            )
        );
    }

}
