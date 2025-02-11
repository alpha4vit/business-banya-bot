package ru.snptech.ritualbitrixbot.telegram;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageConstants {
    public static final String REGISTRATION_FULLNAME_INPUT_MESSAGE = "Введите ваше имя и фамилию";
    public static final String REGISTRATION_REGION_INPUT_MESSAGE = "Выберите ваш регион из списка";
    public static final String REGISTRATION_SENT_TO_MODERATION_MESSAGE = """
            ℹ️ Анкета успешно сформирована и отправлена на модерацию, ожидайте сообщения с результатом\\.
            """;
    public static final String ADMIN_MODERATION_REQUEST_MESSAGE_TEMPLATE = """
            *❗️Получена новая заявка на модерацию*
            
            *Имя:* %s
            *Telegram:* %s
            *Регион:* %s
            """;
    public static final String NO_USERNAME_MESSAGE = """
            ⚠️ Для продолжения взаимодействия с ботом вам необходимо задать username (имя пользователя) в настройках профиля.
            """;
    public static final String MODERATION_APPROVED_TO_USER_MESSAGE = """
            ✅ Анкета одобрена, вам открыт доступ к заявкам и основным функциям бота.
            """;
    public static final String NEW_DEAL_MESSAGE_TEMPLATE = """
            *Новая заявка*
            
            *ID:* %s
            *Источник:* %s
            *Источник 2:* %s
            *Тип сделки:* %s
            """;
    public static final String SHOP_DEAL_MESSAGE_TEMPLATE = """
            *Новая заявка*
            
            *ID:* %s
            *Тип сделки:* %s
            *Источник:* %s
            *Источник 2:* %s
            *Комментарий:* %s
            *Клиент \\(Имя\\):* %s
            *Клиент \\(Телефон\\):* %s
            """;

    public static final String FUNERAL_DEAL_MESSAGE_TEMPLATE = """
            *Новая заявка*
            
            *ID:* %s
            *Тип сделки:* %s
            *Источник:* %s
            *Источник 2:* %s
            *Комментарий:* %s
            *Адрес встречи:* %s
            *Город:* %s
            *Имя заказчика:* %s
            *Фамилия умершего:* %s
            """;

    public static final String FUNERAL_DEAL_WITH_CONTACTS_MESSAGE_TEMPLATE = """
            *Получен телефон по заявке*
            
            *ID:* %s
            *Тип сделки:* %s
            *Источник:* %s
            *Источник 2:* %s
            *Комментарий:* %s
            *Адрес встречи:* %s
            *Город:* %s
            *Имя заказчика:* %s
            *Фамилия умершего:* %s
            *Клиент \\(Имя\\):* %s
            *Клиент \\(Телефон\\):* %s
            """;

    public static final String DATA_SENT_TO_BITRIX_MESSAGE = """
            ✅ Данные переданы в систему
            """;

    public static final String REJECT_INPUT_MESSAGE = """
            Выберите причину отказа из списка ниже
            """;

    public static final String PROBLEM_INPUT_MESSAGE = """
            Опишите проблему
            """;

    public static final String WAITING_AMOUNT_INPUT_MESSAGE = """
            Укажите сумму заказа
            """;

    public static final String WAITING_AMOUNT_CASHED_INPUT_MESSAGE = """
            Укажите сумму комиссии
            """;

    public static final String PHONE_REQUESTED_MESSAGE = """
            ✅ Номер телефона запрошен
            """;

    public static final String MAIN_MENU = """
            Выберите пункт меню
            """;
}
