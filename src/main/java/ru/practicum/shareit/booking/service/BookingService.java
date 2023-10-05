package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

public interface BookingService {
    Booking save(int userId, Booking booking);

    Booking approveItem(int userId, int bookingId, boolean approved);
}
