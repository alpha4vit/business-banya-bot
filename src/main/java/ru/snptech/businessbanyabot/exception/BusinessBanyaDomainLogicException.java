package ru.snptech.businessbanyabot.exception;

public class BusinessBanyaDomainLogicException extends BaseBusinessBanyaException {

    public BusinessBanyaDomainLogicException(final String message) {
        super(message);
    }

    public static class SURVEY_NOT_FOUND extends BusinessBanyaDomainLogicException {
        public SURVEY_NOT_FOUND() {
            super("Анкета не найдена!");
        }
    }

    public static class PHONE_NUMBER_IS_REQUIRED extends BusinessBanyaDomainLogicException {
        public PHONE_NUMBER_IS_REQUIRED() {
            super("Необходимо ввести корректный номер мобильного телефона. Образец: +37544123123");
        }
    }

    public static class INVALID_DEPOSIT_AMOUNT extends BusinessBanyaDomainLogicException {
        public INVALID_DEPOSIT_AMOUNT(String multiplicity) {
            super(
                "Сумма пополнение должна быть валидным целым числом, а также должна быть кратна %s".formatted(multiplicity)
            );
        }
    }

}
