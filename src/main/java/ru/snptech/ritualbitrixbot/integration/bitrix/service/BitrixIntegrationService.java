package ru.snptech.ritualbitrixbot.integration.bitrix.service;

public interface BitrixIntegrationService {
    void problem(String dealId, String problem);
    void reject(String dealId, String rejectReason);
    void inWorkShop(String dealId);
    void inWorkFuneral(String dealId);
    void needPhone(String dealId);
    void successDealShop(String dealId, String amount);
    void successDealFuneral(String dealId, String amount);
    void commissionShop(String dealId, String cashedAmount);
    void commissionFuneral(String dealId, String cashedAmount);
}
