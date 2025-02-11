package ru.snptech.ritualbitrixbot.integration.bitrix.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.snptech.ritualbitrixbot.model.FuneralDealData;
import ru.snptech.ritualbitrixbot.model.ShopDealData;
import ru.snptech.ritualbitrixbot.service.DealService;

import java.util.Map;

import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_ADDRESS;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_CITY;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_COMMENT;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_CONTACT_NAME;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_CONTACT_PHONE;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_CUSTOMER_NAME;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_DEAL_ID;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_DEAL_TYPE;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_DECEASED_SURNAME;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_PHONE;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_REGION;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_SOURCE;
import static ru.snptech.ritualbitrixbot.types.ServiceConstantHolder.BITRIX_RQ_SOURCE_2;

@RestController
@RequestMapping("/api/integration/bitrix")
@RequiredArgsConstructor
public class BitrixIntegrationController {
    private final DealService dealService;

    private final static ResponseEntity<Void> OK = ResponseEntity.ok().build();

    @PostMapping("/shop")
    public ResponseEntity<Void> createShopDeal(@RequestParam Map<String, Object> params) {
        dealService.initShopDeal(new ShopDealData(
                BITRIX_RQ_DEAL_ID.getValue(params),
                BITRIX_RQ_SOURCE.getValue(params),
                BITRIX_RQ_SOURCE_2.getValue(params),
                BITRIX_RQ_DEAL_TYPE.getValue(params),
                BITRIX_RQ_COMMENT.getValue(params),
                BITRIX_RQ_CONTACT_NAME.getValue(params),
                BITRIX_RQ_CONTACT_PHONE.getValue(params),
                BITRIX_RQ_REGION.getValue(params)
        ));
        return OK;
    }

    @PostMapping("/funeral")
    public ResponseEntity<Void> createFuneralDeal(@RequestParam Map<String, Object> params) {
        dealService.initFuneralDeal(new FuneralDealData(
                BITRIX_RQ_DEAL_ID.getValue(params),
                BITRIX_RQ_SOURCE.getValue(params),
                BITRIX_RQ_SOURCE_2.getValue(params),
                BITRIX_RQ_DEAL_TYPE.getValue(params),
                BITRIX_RQ_COMMENT.getValue(params),
                BITRIX_RQ_ADDRESS.getValue(params),
                BITRIX_RQ_CITY.getValue(params),
                BITRIX_RQ_CUSTOMER_NAME.getValue(params),
                BITRIX_RQ_DECEASED_SURNAME.getValue(params),
                BITRIX_RQ_REGION.getValue(params)
        ));
        return OK;
    }

    @PostMapping("/phone")
    public ResponseEntity<Void> phoneReceived(@RequestParam Map<String, Object> params) {
        dealService.updateDealPhone(
                BITRIX_RQ_DEAL_ID.getValue(params),
                BITRIX_RQ_CONTACT_NAME.getValue(params),
                BITRIX_RQ_CONTACT_PHONE.getValue(params)
        );
        return OK;
    }

}
