package ru.practicum.shareit.user.exception;

/**
 * Исключение, выбрасываемое, когда пользователь уже существует.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
