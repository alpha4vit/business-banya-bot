package ru.snptech.businessbanyabot.integration.bitrix.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.snptech.businessbanyabot.integration.bitrix.client.BitrixCrmClient;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.BitrixEntityType;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixCompanyDto;
import ru.snptech.businessbanyabot.integration.bitrix.dto.company.BitrixCompanyStatus;
import ru.snptech.businessbanyabot.integration.bitrix.dto.filter.BitrixFilter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.filter.BitrixListFilter;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BitrixIntegrationService {

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
            List.of(
                "ID",
                "TITLE",
                "HAS_PHONE",
                "HAS_EMAIL",
                "HAS_IMOL",
                "ASSIGNED_BY_ID",
                "CREATED_BY_ID",
                "MODIFY_BY_ID",
                "INDUSTRY",
                "REVENUE",
                "CURRENCY_ID",
                "EMPLOYEES",
                "DATE_CREATE",
                "DATE_MODIFY",
                "OPENED",
                "IS_MY_COMPANY",
                "LAST_ACTIVITY_BY",
                "LAST_ACTIVITY_TIME",
                "UF_CRM_EMPLOYEES",
                "UF_CRM_1740029712849",
                "UF_CRM_1744002909175",
                "UF_CRM_1741586457426",
                "UF_CRM_1744003826501",
                "UF_CRM_1744062873266",
                "UF_CRM_1751618553855",
                "UF_CRM_1753564350706",
                "UF_CRM_1753564549812",
                "UF_CRM_1753564743659",
                "UF_CRM_1753564819600",
                "UF_CRM_1753564972697",
                "UF_CRM_1753565034091",
                "UF_CRM_1753565978360",
                "UF_CRM_1753566020803",
                "UF_CRM_1753566191776",
                "UF_CRM_1753566483937",
                "UF_CRM_1753566506757",
                "UF_CRM_1753566529182",
                "UF_CRM_1753566550751",
                "UF_CRM_1753566595468",
                "UF_CRM_1753566733417",
                "UF_CRM_1753566862458",
                "UF_CRM_1753567007871",
                "UF_CRM_1753567018433",
                "UF_CRM_1753567192377",
                "UF_CRM_1753567278281",
                "UF_CRM_1753567364294",
                "UF_CRM_1753567456712",
                "UF_CRM_1753567528920",
                "UF_CRM_1753567605826",
                "UF_CRM_1753626792826",
                "PHONE"
            ),
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
}
