package ru.snptech.businessbanyabot.integration.bitrix.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.snptech.businessbanyabot.integration.bitrix.dto.BitrixCompanyDto;
import ru.snptech.businessbanyabot.integration.bitrix.dto.BitrixFilter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.BitrixResponse;

import java.util.List;
import java.util.Map;

@FeignClient(
    name = "bitrix-company-feign-client",
    url = "${application.integration.bitrix.url}",
    configuration = FeignBitrixClientConfiguration.class
)
public interface BitrixCrmClient {

    @GetMapping("/hepp0ysnq72g56zu/crm.company.list.json")
    ResponseEntity<BitrixResponse<List<BitrixCompanyDto>>> getAllCompanies();

    @GetMapping("/wr0nsrymtgpphupm/crm.company.list.json")
    ResponseEntity<BitrixResponse<List<BitrixCompanyDto>>> getCompaniesByResidentStatus(
        @RequestParam("filter[UF_CRM_1740029712849]") String status
    );

    @GetMapping("/wr0nsrymtgpphupm/crm.company.get.json")
    ResponseEntity<BitrixResponse<BitrixCompanyDto>> getCompanyById(
        @RequestParam("id") String companyId
    );

    @PostMapping("/hepp0ysnq72g56zu/crm.duplicate.findbycomm.json")
    ResponseEntity<BitrixResponse<Map<String, List<String>>>> findByFilter(
        @RequestBody BitrixFilter filter
    );
}
