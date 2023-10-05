package ru.practicum.shareit.booking;

public class BookingBadRequestException extends RuntimeException {
    public BookingBadRequestException(String message) {
        super(message);
    }
}
