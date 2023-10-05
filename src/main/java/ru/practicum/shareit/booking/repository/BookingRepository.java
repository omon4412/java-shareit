package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Collection<Booking> findAllByBookerIdOrderByIdDesc(int bookerId);

    Collection<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(
            int booker_id, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByIdDesc(int booker_id, LocalDateTime end);

    Collection<Booking> findAllByBookerIdAndStartAfterOrderByIdDesc(Integer userId, LocalDateTime now);

    Collection<Booking> findAllByBookerIdAndStatusIs(Integer userId, BookingStatus state);

    Collection<Booking> findAllByItemOwnerIdOrderByIdDesc(int ownerId);

    Collection<Booking> findAllByItemOwnerIdAndStatusIs(Integer ownerId, BookingStatus bookingStatus);

    Collection<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Integer ownerId, LocalDateTime now, LocalDateTime now1);

    Collection<Booking> findAllByItemOwnerIdAndEndBeforeOrderByIdDesc(Integer ownerId, LocalDateTime now);

    Collection<Booking> findAllByItemOwnerIdAndStartAfterOrderByIdDesc(Integer ownerId, LocalDateTime now);
}
