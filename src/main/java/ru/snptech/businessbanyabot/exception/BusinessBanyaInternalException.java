package ru.snptech.businessbanyabot.exception;

public final class BusinessBanyaInternalException {

    public static class MESSAGE_HAS_NO_CONTENT extends RuntimeException {
        public MESSAGE_HAS_NO_CONTENT() {
            super("Ошибка, сообщение не содержит данных, попробуйте снова!");
        }
    }

}
