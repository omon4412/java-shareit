package ru.practicum.shareit.item.exception;

/**
 * Исключение, выбрасываемое, когда вещь не доступна.
 */
public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}
