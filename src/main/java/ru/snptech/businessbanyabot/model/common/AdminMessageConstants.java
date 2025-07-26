package ru.snptech.businessbanyabot.model.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AdminMessageConstants {

    public static final String NEW_SURVEY_MESSAGE_TEMPLATE = """
        *Новая заявка*
                    
        *ID:* %s
        *ФИО:* %s
        *Номер мобильного телефона:* %s
        *Социальная сеть:* %s
        """;

    public static final String SURVEY_ACCEPT_MESSAGE = "Анкета успешно одобрена\\. Ожидание оплаты от пользователя\\.";

    public static final String SURVEY_DECLINED_MESSAGE = "Анкета успешно отклонена\\. Пользователь заблокирован до N";

}
