package ru.practicum.shareit.item.exception;

/**
 * Исключение, выбрасываемое, когда к вещи обращается не тот пользователь (не владелец).
 */
public class WrongOwnerException extends RuntimeException {
    public WrongOwnerException(String message) {
        super(message);
    }
}
