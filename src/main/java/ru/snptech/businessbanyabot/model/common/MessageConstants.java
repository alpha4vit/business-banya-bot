package ru.snptech.businessbanyabot.model.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageConstants {

    public static final String PHONE_NUMBER_IS_REQUIRED = "Необходимо ввести корректный номер мобильного телефона";
    public static final String REGISTRATION_REGION_INPUT_MESSAGE = "Выберите ваш регион из списка";
    public static final String REGISTRATION_CONFIRMATION_INPUT_MESSAGE = """
        *Имя:* %s
        *Регион:* %s
                    
        Подтвердите данные или скорректируйте
        """;
    public static final String REGISTRATION_SENT_TO_MODERATION_MESSAGE = """
        ℹ️ Анкета успешно сформирована и отправлена на модерацию, ожидайте сообщения с результатом\\.
        """;
    public static final String ADMIN_MODERATION_REQUEST_MESSAGE_TEMPLATE = """
        *❗️Получена новая заявка на модерацию*
                    
        *Имя:* %s
        *Telegram:* %s
        *Регион:* %s
        """;

    public static final String VERIFICATION_NEED_MESSAGE = """
        ⚠️ Для продолжения взаимодействия с ботом вам необходимо принять пользовательское соглашение и ввести номер мобильного телефона.
                    
        ССЫЛКА
        """;
    public static final String SURVEY_COMPLETE_MESSAGE
        = "Анкета успешно заполнена! Ожидайте уведомления о результате прохождения модерации.";

    public static final String SURVEY_ACCEPTED_CHOOSE_PAYMENT_METHOD
        = "Ваше анкета одобрена администратором. Выберите желаемый способ оплаты";

    public static final String FAST_PAYMENT_TEMPLATE = """
        *Стоимость:* %s %s
        *Номер QR-кода:* %s
        *Действителен до:* %s
        *Ссылка на оплату:* %s
        """;

    public static final String SEARCH_MENU_MESSAGE = "Введите ФИО искомого резидента";

    public static final String NO_RESIDENTS_FIND =
        "Резиденты с заданными параметрами не найдены! Попробуйте изменить запрос";

    public static final String USER_CARD_PREVIEW = """
        *Имя пользователя: * %s
        *Номер мобильного телефона: * %s
        *Текущий бизнес: * %s
        *Ключевой актив: * %s
        """;

    public static final String MODERATION_APPROVED_TO_USER_MESSAGE = """
        ✅ Анкета одобрена, вам открыт доступ к заявкам и основным функциям бота.
        """;


    public static final String SHOP_DEAL_MESSAGE_TEMPLATE = """
        *ID:* %s
        *Тип сделки:* %s
        *Источник 2:* %s
        *Комментарий:* %s
        *Клиент (Имя):* %s
        *Клиент (Телефон):* %s
        """;

    public static final String NEW_SHOP_DEAL_MESSAGE_TEMPLATE = """
        *Новая заявка*
                    
        """ + SHOP_DEAL_MESSAGE_TEMPLATE;

    public static final String FUNERAL_DEAL_MESSAGE_TEMPLATE = """
        *ID:* %s
        *Тип сделки:* %s
        *Источник 2:* %s
        *Комментарий:* %s
        *Адрес встречи:* %s
        *Город:* %s
        *Фамилия умершего:* %s
        """;

    public static final String NEW_FUNERAL_DEAL_MESSAGE_TEMPLATE = """
        *Новая заявка*
                    
        """ + FUNERAL_DEAL_MESSAGE_TEMPLATE;

    public static final String FUNERAL_DEAL_WITH_CONTACTS_MESSAGE_TEMPLATE = """
        *Получен телефон по заявке*
                    
        *ID:* %s
        *Тип сделки:* %s
        *Источник 2:* %s
        *Комментарий:* %s
        *Адрес встречи:* %s
        *Город:* %s
        *Фамилия умершего:* %s
        *Клиент \\(Имя\\):* %s
        *Клиент \\(Телефон\\):* %s
        """;

    public static final String DATA_SENT_TO_BITRIX_MESSAGE = """
        %s
                    
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
        %s
                    
        ✅ По сделке запрошен номер телефона
        """;

    public static final String DEAL_AMOUNT_ENTERED_MESSAGE = """
        %s
                    
        ✅ По сделке указана сумма сделки %s
        """;

    public static final String DEAL_COMMISSION_ENTERED_MESSAGE = """
        %s
                    
        ✅ По сделке указана сумма комиссии %s
        """;

    public static final String DEAL_FINISHED_MESSAGE = """
        %s
                    
        ✅ Сделка завершена\\!
        """;

    public static final String DEAL_SUCCESS_MESSAGE = """
        %s
                    
        ✅ Сделка успешна\\! Ожидается ввод суммы заказа
        """;

    public static final String DEAL_PROBLEM_MESSAGE = """
        %s
                    
        ❌ Проблема по сделке\\! Проблема: %s
        """;

    public static final String DEAL_REJECT_MESSAGE = """
        %s
                    
        ❌ Отказ по сделке\\! Причина: %s
        """;

    public static final String MAIN_MENU = """
        Выберите пункт меню
        """;
}
