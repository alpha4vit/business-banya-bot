package ru.snptech.businessbanyabot.integration.bitrix.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.snptech.businessbanyabot.integration.bitrix.dto.BitrixCrmDealUpdate;

@FeignClient(
        name = "bitrixIntegrationClient",
        url = "https://ritual-online.ru/rest/2230/hlx26gvitwotbh9x"
)
public interface BitrixIntegrationClient {


    @PostMapping("/crm.deal.update.json")
    ResponseEntity<String> updateDeal(@RequestBody BitrixCrmDealUpdate bitrixCrmDealUpdate);

}
