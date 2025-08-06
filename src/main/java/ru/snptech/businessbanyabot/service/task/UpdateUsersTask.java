package ru.snptech.businessbanyabot.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixCompanyStatus;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.ResidentStatus;
import ru.snptech.businessbanyabot.integration.bitrix.service.BitrixIntegrationService;
import ru.snptech.businessbanyabot.integration.bitrix.util.LabeledEnumUtil;
import ru.snptech.businessbanyabot.repository.UserRepository;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdateUsersTask {

    private final BitrixIntegrationService bitrixIntegrationService;
    private final UserRepository userRepository;

    // TODO remove 100, set 1
    @Transactional
    @Scheduled(fixedRate = 100, timeUnit = TimeUnit.MINUTES)
    public void updateResidents() {
        var companies = bitrixIntegrationService.findAllCompaniesByStatus(BitrixCompanyStatus.RESIDENT)
            .stream()
            .filter(company -> company.phoneList() != null && !company.phoneList().isEmpty())
            .collect(Collectors.toMap(
                company -> company.phoneList().getFirst().value(),
                Function.identity()
            ));

        var phoneNumbers = companies.values()
            .stream()
            .map(company -> company.phoneList().getFirst().value())
            .toList();

        var users = userRepository.findByPhoneNumberIn(phoneNumbers);

        users.forEach((user) -> {
            var company = companies.remove(user.getPhoneNumber());
            var residentStatus = LabeledEnumUtil.fromId(ResidentStatus.class, company.residentStatus());

            user.setInfo(company);
            user.setFullName(company.title());
            user.setRole(residentStatus.toUserRole());

            if (!company.phoneList().isEmpty()) {
                user.setPhoneNumber(company.phoneList().getFirst().value());
            }
        });

        userRepository.saveAll(users);
    }
}
