package ru.practicum.shareit.booking.exception;

/**
 * Исключение, выбрасываемое, когда бронь не существует.
 */
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
