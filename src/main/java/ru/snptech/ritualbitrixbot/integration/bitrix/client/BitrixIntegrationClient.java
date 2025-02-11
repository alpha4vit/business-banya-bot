package ru.snptech.ritualbitrixbot.integration.bitrix.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.snptech.ritualbitrixbot.integration.bitrix.dto.BitrixCrmDealUpdate;

@FeignClient(
        name = "bitrixIntegrationClient",
        url = "https://ritual-online.ru/rest/2230/hlx26gvitwotbh9x"
)
public interface BitrixIntegrationClient {


    @PostMapping("/crm.deal.update.json")
    ResponseEntity<String> updateDeal(@RequestBody BitrixCrmDealUpdate bitrixCrmDealUpdate);

}
