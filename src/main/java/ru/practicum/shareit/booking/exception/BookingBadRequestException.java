package ru.practicum.shareit.booking.exception;

/**
 * Исключение, выбрасываемое, когда есть ошибка в брони.
 */
public class BookingBadRequestException extends RuntimeException {
    public BookingBadRequestException(String message) {
        super(message);
    }
}
