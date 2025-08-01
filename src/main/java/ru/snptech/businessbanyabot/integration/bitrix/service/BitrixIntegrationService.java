package ru.snptech.businessbanyabot.integration.bitrix.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.snptech.businessbanyabot.integration.bitrix.client.BitrixCrmClient;
import ru.snptech.businessbanyabot.integration.bitrix.dto.BitrixCompanyDto;
import ru.snptech.businessbanyabot.integration.bitrix.dto.BitrixEntityType;
import ru.snptech.businessbanyabot.integration.bitrix.dto.BitrixFilter;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BitrixIntegrationService {

    private final BitrixCrmClient crmClient;

    public Optional<BitrixCompanyDto> findCompanyByPhoneNumber(String phoneNumber) {
        var response = crmClient.findByFilter(
            new BitrixFilter(
                BitrixEntityType.COMPANY.name(),
                "PHONE",
                List.of(phoneNumber)
            )
        ).getBody();

        if (!(response.result() instanceof Map<?, ?>)) return Optional.empty();

        var result = (Map<String, List<Integer>>) response.result();

        var companyIds = result.get(BitrixEntityType.COMPANY.name());

        if (companyIds == null || companyIds.isEmpty()) {
            return Optional.empty();
        }

        var matchedCompanies = companyIds.stream()
            .map((id) -> crmClient.getCompanyById(String.valueOf(id)).getBody().result())
            .toList();

        return matchedCompanies.stream().max(Comparator.comparing(o -> Instant.parse(o.dateCreate())));
    }
}
