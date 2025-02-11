package ru.snptech.ritualbitrixbot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RejectReason {
    USER_DEAL_REJECT_CUSTOMER_CHANGED_MIND("UDRCCM_", "Заказчик передумал", "66"), // Заказчик передумал
    USER_DEAL_REJECT_ANOTHER_AGENT_ON_ADDRESS("UDRAAOA_", "На адресе был другой агент", "67"), // На адресе был другой агент
    USER_DEAL_REJECT_NO_CALL_ANSWER("UDRNCA_", "Не отвечают на звонки", "68"), // Не отвечают на звонки
    USER_DEAL_REJECT_NO_TOUCH("UDRNT_", "Не удалось связаться", "69"), // Не удалось связаться
    USER_DEAL_REJECT_BAD_PRICE("UDRBP_", "Не устроила цена", "70"), // Не устроила цена
    USER_DEAL_REJECT_NO_MONEY("UDRNM_", "Нет денег", "71"), // Нет денег
    USER_DEAL_REJECT_REFUSED_WHILE_TRANSFER("UDRRWT_", "Отказались пока ехал", "72"), // Отказались пока ехал
    USER_DEAL_REJECT_ANOTHER_COMPANY("UDRAC_", "Оформили сами в другой компании", "73"), // Оформили сами в другой компании
    USER_DEAL_REJECT_FRIENDS_HELP("UDRFH_", "Оформили через знакомых", "74"), // Оформили через знакомых
    USER_DEAL_REJECT_ANOTHER_AGENT_INTERRUPTED("UDRAAI_", "Перебил другой агент", "75"), // Перебил другой агент
    USER_DEAL_REJECT_MORGUE_INTERRUPTED("UDRMI_", "Перебили в морге", "76"), // Перебили в морге
    USER_DEAL_REJECT_DONT_LET_GO("UDRDLG_", "Приехал. Не пустили", "77"), // Приехал. Не пустили
    USER_DEAL_REJECT_OWN_AGENT("UDROA_", "Есть свой агент", "79"), // Есть свой агент
    USER_DEAL_REJECT_THIRD_PERSON("UDRTP_", "3-е лицо", "560"), // 3-е лицо
    USER_DEAL_REJECT_CONSULTATION("UDRC_", "Консультация", "589"), // Консультация
    USER_DEAL_REJECT_BAD_DELIVERY_PRICE("UDRBDP_", "Не устроила сумма доставки", "918"), // Не устроила сумма доставки
    USER_DEAL_REJECT_BAD_DELIVERY_TIME("UDRBDT_", "Не устроило время доставки", "919"); // Не устроило время доставки
    
    private final String callbackPrefix;
    private final String lexem;
    private final String bitrixValue;
}
