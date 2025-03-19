package ru.snptech.ritualbitrixbot.integration.bitrix.service;

public interface BitrixIntegrationService {
    void problem(String dealId, String problem);
    void reject(String dealId, String rejectReason);
    void inWorkShop(String dealId);
    void inWorkFuneral(String dealId);
    void needPhone(String dealId);
    void successDealShop(String dealId);
    void successDealFuneral(String dealId);
    void updateDealAmount(String dealId, String amount);
    void commissionShop(String dealId);
    void commissionFuneral(String dealId);
    void updateDealCommission(String dealId, String cashedAmount);
}
