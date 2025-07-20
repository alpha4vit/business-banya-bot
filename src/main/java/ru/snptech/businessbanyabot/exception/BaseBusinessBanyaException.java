package ru.snptech.businessbanyabot.exception;

import lombok.Getter;

@Getter
public abstract class BaseBusinessBanyaException extends RuntimeException {
    public BaseBusinessBanyaException(final String message) {
        super(message);
    }

}
