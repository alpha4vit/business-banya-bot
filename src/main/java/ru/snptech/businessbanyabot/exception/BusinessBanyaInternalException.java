package ru.snptech.businessbanyabot.exception;

public class BusinessBanyaInternalException extends BaseBusinessBanyaException {


    public BusinessBanyaInternalException(final String message) {
        super(message);
    }

    public static class MESSAGE_HAS_NO_CONTENT extends BusinessBanyaInternalException {
        public MESSAGE_HAS_NO_CONTENT() {
            super("Ошибка, сообщение не содержит данных, попробуйте снова!");
        }
    }

    public static class SURVEY_QUESTION_NOT_FOUND extends BusinessBanyaInternalException {
        public SURVEY_QUESTION_NOT_FOUND() {
            super("Вопрос не найден в базе. Начните заоплнение анкеты заново.");
        }
    }

    public static class REPORT_INPUT_CANNOT_BE_NULL extends BusinessBanyaInternalException {
        public REPORT_INPUT_CANNOT_BE_NULL() {
            super("Входные данные для получения отчета не могут быть пустыми!");
        }
    }
}
