package ru.practicum.shareit.request;

/**
 * Исключение, выбрасываемое, когда запрос не существует.
 */
public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String message) {
        super(message);
    }
}
