package ru.snptech.businessbanyabot.model.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageConstants {

    public static final String VERIFICATION_NEED_MESSAGE = """
        ⚠️ Для продолжения взаимодействия с ботом вам необходимо принять пользовательское соглашение.
        ⚠️ Введите номер мобильного телефона, связанный в WhatsApp, подтверждая соглашение.
        """;
    public static final String SURVEY_COMPLETE_MESSAGE
        = "✅ Анкета успешно заполнена! Ожидайте уведомления о результате прохождения модерации.";

    public static final String SURVEY_ACCEPTED_CHOOSE_PAYMENT_METHOD
        = " ✅ Ваше анкета одобрена администратором. Выберите желаемый способ оплаты";

    public static final String FAST_PAYMENT_TEMPLATE = """
        *Стоимость:* %s %s
        *Номер QR-кода:* %s
        *Действителен до:* %s
        *Ссылка на оплату:* %s
        """;

    public static final String FAST_PAYMENT_EXPIRED_TEMPLATE =
        "Возможно вы забыли завершить оплату по данному платежу:\n"
            + FAST_PAYMENT_TEMPLATE;

    public static final String FAST_PAYMENT_ALREADY_EXISTS_TEMPLATE =
        "*У вас имеется незавершенный платеж! Погасите его или отмените!*\n"
            + FAST_PAYMENT_TEMPLATE;

    public static final String SEARCH_MENU_MESSAGE = "Введите ФИО или номер телефона искомого резидента";

    public static final String NO_RESIDENTS_FIND =
        "Резиденты с заданными параметрами не найдены! Попробуйте изменить запрос";

    public static final String NO_FUTURE_EVENTS_FIND =
        "Запланированные мероприятия не найдены!";

    public static final String USER_CARD_PREVIEW = """
        *Имя пользователя: * %s
        *Дата рождения: * %s
        *Номер мобильного телефона: * %s
        *Ник Telegram* %s
        *Чат WhatsApp* %s
        *Текущий бизнес: * %s
        """;


    public static final String MAIN_MENU = """
        Выберите пункт меню
        """;

    public static final String DEPOSIT_MONEY_INPUT
        = "Введите сумму депозита";

    public static final String BALANCE_MESSAGE
        = """
        *Ваш баланс:* %s
        *Ваши баллы:* %s
        """;

    public static final String NOTIFICATION_CONTENT_MESSAGE
        = "Введите содержимое сообщения для рассылки";

    public static final String NOTIFICATION_CONSUMER_MESSAGE
        = "Введите ФИО или номера телефонов получателей рассылки";

    public static final String CONSUMERS_NOT_FOUND_BY_FULL_NAMES
        = "Пользователи с данными ФИО или номерами телефонов не найдены: \n\n%s \n\nПопробуйте снова или запустите рассылку";

    public static final String NOTIFICATION_SUCCESSFULLY_SENT
        = "Рассылка успешно отправлена!";

    public static final String SELECT_REPORT_TYPE_MESSAGE
        = "Выберите тип фильтра для получения отчета.";

    public static final String SELECT_REPORT_TYPE_PARAM_MESSAGE
        = "Выберите параметр фильтра для получения отчета.";

    public static final String LEADERBOARD_HEADER = """
        *Ваша позиция в рейтинге:* %s
        *Ваши баллы:* %s
        """;

    public static final String LEADERBOARD_ROW_TEMPLATE
        = """
                
        *Позиция* %s
        *ФИО:* %s
        *Номер телефона:* %s
        *Количество баллов:* %s
        """;

    public static final String SELECT_EVENT_TYPE_MESSAGE
        = "Выберите тип события";


    public static final String PENDING_PAYMENT_SUCCESSFULLY_DECLINED
        = "✅ Оплата успешно отменена";


    public static final String ACCOUNT_WAS_BANNED
        = "Ваш аккаунт заблокирован до %s";

    public static final String RESIDENT_SUBSCRIPTION_CONTINUATION_TEMPLATE = """
        Ваш статус резидента истекает через *%s*
        Предлагаем вам продлить его на *%s*.
        
        Сумма платежа составит *%s*
        """;

}
