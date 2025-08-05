package ru.snptech.businessbanyabot.integration.bitrix.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.snptech.businessbanyabot.integration.bitrix.client.BitrixCrmClient;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.BitrixEntityType;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixCompanyDto;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixCompanyStatus;
import ru.snptech.businessbanyabot.integration.bitrix.dto.event.BitrixEventDto;
import ru.snptech.businessbanyabot.integration.bitrix.dto.filter.BitrixFilter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.filter.BitrixListFilter;
import ru.snptech.businessbanyabot.integration.bitrix.properties.BitrixProperties;
import ru.snptech.businessbanyabot.integration.bitrix.util.CompanyFields;
import ru.snptech.businessbanyabot.integration.bitrix.util.EventFields;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BitrixIntegrationService {

    private final BitrixProperties bitrixProperties;
    private final BitrixCrmClient crmClient;

    private static final Integer PAGE_SIZE = 50;

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

    public List<BitrixCompanyDto> findAllCompaniesByStatus(BitrixCompanyStatus status) {
        var total = PAGE_SIZE;
        var cursor = 0;

        List<BitrixCompanyDto> result = new ArrayList<>();

        var filter = new BitrixListFilter(
            Map.of(status.getBitrixFieldName(), status.getBitrixId()),
            CompanyFields.SELECT,
            cursor
        );

        while (result.size() < total) {
            var response = crmClient.findCompaniesByFilter(filter);

            result.addAll(response.getBody().result());

            cursor += PAGE_SIZE;
            filter.setStart(cursor);
            total = response.getBody().total();
        }

        return result;
    }

    public List<BitrixEventDto> findAllEvents() {
        var total = PAGE_SIZE;
        var cursor = 0;

        List<BitrixEventDto> result = new ArrayList<>();

        var filter = new BitrixListFilter(
            EventFields.EVENT_FILTER,
            EventFields.SELECT,
            cursor
        );

        while (result.size() < total) {
            var response = crmClient.findEvents(filter);

            result.addAll(response.getBody().result());

            cursor += PAGE_SIZE;
            filter.setStart(cursor);
            total = response.getBody().total();
        }

        return result;
    }
}
