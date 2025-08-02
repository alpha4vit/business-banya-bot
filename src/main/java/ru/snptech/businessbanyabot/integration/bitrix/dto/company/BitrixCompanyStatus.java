package ru.snptech.businessbanyabot.integration.bitrix.dto.company;

import lombok.Getter;

@Getter
public enum BitrixCompanyStatus {
    RESIDENT("281042"),
    NON_RESIDENT("281044"),
    EX_RESIDENT("281222");

    private final String bitrixId;

    private final String bitrixFieldName = "UF_CRM_1740029712849";

    BitrixCompanyStatus(String bitrixId) {
        this.bitrixId = bitrixId;
    }
}
