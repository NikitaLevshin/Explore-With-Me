package ru.yandex.practicum.exceptions;

public class WrongTimeException extends RuntimeException {
    public WrongTimeException(String message) {
        super(message);
    }
}
