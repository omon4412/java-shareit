package ru.practicum.shareit.item.exception;

/**
 * Исключение, выбрасываемое, когда предмет не существует.
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
