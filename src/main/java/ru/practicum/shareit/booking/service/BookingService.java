package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Collection;

public interface BookingService {
    Booking save(int userId, Booking booking);

    Booking changeStatus(int userId, int bookingId, boolean approved);

    Booking getById(Integer userId, int bookingId);

    Collection<Booking> getAll(Integer userId, BookingStatus state);

    Collection<Booking> getAllByOwner(Integer ownerId, BookingStatus bookingStatus);
}
