package ru.snptech.businessbanyabot.model.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageConstants {

    public static final String VERIFICATION_NEED_MESSAGE = """
        ⚠️ Для продолжения взаимодействия с ботом вам необходимо принять пользовательское соглашение и ввести номер мобильного телефона.
                    
        ССЫЛКА
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

    public static final String SEARCH_MENU_MESSAGE = "Введите ФИО искомого резидента";

    public static final String NO_RESIDENTS_FIND =
        "Резиденты с заданными параметрами не найдены! Попробуйте изменить запрос";

    public static final String NO_FUTURE_EVENTS_FIND =
        "Запланированные мероприятия не найдены!";

    public static final String USER_CARD_PREVIEW = """
        *Имя пользователя: * %s
        *Номер мобильного телефона: * %s
        *Текущий бизнес: * %s
        *Ключевой актив: * %s
        """;


    public static final String MAIN_MENU = """
        Выберите пункт меню
        """;

    public static final String DEPOSIT_MONEY_INPUT
        = "Введите сумму депозита";

    public static final String BALANCE_MESSAGE
        = "*Ваш баланс:* %s";

    public static final String NOTIFICATION_CONTENT_MESSAGE
        = "Введите содержимое сообщения для рассылки";

    public static final String NOTIFICATION_CONSUMER_MESSAGE
        = "Введите ФИО получателей рассылки через запятую как в Bitrix";

    public static final String CONSUMERS_NOT_FOUND_BY_FULL_NAMES
        = "Пользователи с данными ФИО не найдены: \n\n%s \n\nПопробуйте снова или запустите рассылку";

    public static final String NOTIFICATION_SUCCESSFULLY_SENT
        = "Рассылка успешно отправлена!";
}
