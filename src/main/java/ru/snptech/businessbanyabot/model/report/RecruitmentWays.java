package ru.snptech.businessbanyabot.model.report;

import lombok.Getter;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.LabeledEnum;

@Getter
public enum RecruitmentWays implements LabeledEnum {
    SOCIAL_MEDIA("281610", "Соцсети"),
    CONTEXT_ADS("281612", "Контекстная реклама (Google Ads, Яндекс.Директ)"),
    COLD_CALLS_EMAILS("281614", "Холодные звонки/email-рассылки"),
    RECOMMENDATIONS("281616", "Рекомендации и сарафанное радио"),
    AVITO_YULA("281618", "Авито/Юла"),
    OTHER("281620", "Другое");

    private final String id;
    private final String label;

    RecruitmentWays(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
