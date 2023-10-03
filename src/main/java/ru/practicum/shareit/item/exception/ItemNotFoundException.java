package ru.practicum.shareit.item.exception;

/**
 * Исключение, выбрасываемое, когда вещь не существует.
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
