package ru.practicum.shareit.user.exception;

/**
 * Исключение, выбрасываемое, когда пользователь не существует.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
