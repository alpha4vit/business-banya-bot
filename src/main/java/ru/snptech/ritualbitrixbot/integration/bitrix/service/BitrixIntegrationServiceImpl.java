package ru.snptech.ritualbitrixbot.integration.bitrix.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.snptech.ritualbitrixbot.integration.bitrix.client.BitrixIntegrationClient;
import ru.snptech.ritualbitrixbot.integration.bitrix.dto.BitrixCrmDealUpdate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BitrixIntegrationServiceImpl implements BitrixIntegrationService {
    private final BitrixIntegrationClient bitrixIntegrationClient;
    @Override
    public void problem(String dealId, String problem) {
        final var PROBLEM_STATUS_ID = "C1:UC_ZTXAB8";
        updateStatus(dealId, PROBLEM_STATUS_ID);
        final var AGENT_COMMENT_FIELD_ID = "UF_CRM_1716184185";
        updateCustomField(dealId, AGENT_COMMENT_FIELD_ID, problem);
    }

    @Override
    public void reject(String dealId, String rejectReason) {
        final var REJECT_STATUS_ID = "C1:UC_PHAGOX";
        final var REJECT_REASON_FIELD_ID = "UF_CRM_1715683691";
        updateCustomField(dealId, REJECT_REASON_FIELD_ID, rejectReason);
        updateStatus(dealId, REJECT_STATUS_ID);
    }

    @Override
    public void inWorkShop(String dealId) {
        final var SHOP_IN_WORK_STATUS_ID = "C1:EXECUTING";
        updateStatus(dealId, SHOP_IN_WORK_STATUS_ID);
    }

    @Override
    public void inWorkFuneral(String dealId) {
        final var FUNERAL_IN_WORK_STATUS_ID = "C1:UC_28UYH9";
        updateStatus(dealId, FUNERAL_IN_WORK_STATUS_ID);
    }

    @Override
    public void needPhone(String dealId) {
        final var NEED_PHONE_STATUS_ID = "C1:UC_BXQD0I";
        updateStatus(dealId, NEED_PHONE_STATUS_ID);
    }

    @Override
    public void successDealShop(String dealId, String amount) {
        final var SHOP_SUCCESS_STATUS_ID = "C1:UC_Y23RVD";
        updateCustomField(dealId, "OPPORTUNITY", amount);
        updateCustomField(dealId, "CURRENCY_ID", "RUB");
        updateStatus(dealId, SHOP_SUCCESS_STATUS_ID);
    }

    @Override
    public void successDealFuneral(String dealId, String amount) {
        final var FUNERAL_SUCCESS_STATUS_ID = "C1:UC_ZJD009";
        updateCustomField(dealId, "OPPORTUNITY", amount);
        updateCustomField(dealId, "CURRENCY_ID", "RUB");
        updateStatus(dealId, FUNERAL_SUCCESS_STATUS_ID);
    }

    @Override
    public void commissionShop(String dealId, String cashedAmount) {
        final var SHOP_COMMISSION_STATUS_ID = "C1:UC_FGWLQ4";
        final var SHOP_COMMISSION_CASHED_AMOUNT_FIELD_ID = "UF_CRM_1735195362";
        updateCustomField(dealId, SHOP_COMMISSION_CASHED_AMOUNT_FIELD_ID, cashedAmount);
        updateStatus(dealId, SHOP_COMMISSION_STATUS_ID);
    }

    @Override
    public void commissionFuneral(String dealId, String cashedAmount) {
        final var FUNERAL_COMMISSION_STATUS_ID = "C1:UC_ZC3KN2";
        final var FUNERAL_COMMISSION_CASHED_AMOUNT_FIELD_ID = "UF_CRM_1735195362";
        updateCustomField(dealId, FUNERAL_COMMISSION_CASHED_AMOUNT_FIELD_ID, cashedAmount);
        updateStatus(dealId, FUNERAL_COMMISSION_STATUS_ID);
    }

    private void updateStatus(String dealId, String status) {
        final var STATUS_FIELD_ID = "STAGE_ID";
        updateCustomField(dealId, STATUS_FIELD_ID, status);
    }

    private void updateCustomField(String dealId, String fieldId, String value) {
        var body = new BitrixCrmDealUpdate(Long.parseLong(dealId), Map.of(fieldId, value));
        log.info("Request to bitrix: {}", body);
        var rs = bitrixIntegrationClient.updateDeal(body);
        log.info("Response from bitrix: {}", rs);
    }
}
