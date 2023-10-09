package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Collection<Booking> findAllByBookerIdOrderByIdDesc(int bookerId);

    Collection<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(
            int bookerId, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByIdDesc(int bookerId, LocalDateTime end);

    Collection<Booking> findAllByBookerIdAndStartAfterOrderByIdDesc(Integer userId, LocalDateTime now);

    Collection<Booking> findAllByBookerIdAndStatusIs(Integer userId, BookingStatus state);

    Collection<Booking> findAllByItemOwnerIdOrderByIdDesc(int ownerId);

    Collection<Booking> findAllByItemOwnerIdAndStatusIs(Integer ownerId, BookingStatus bookingStatus);

    Collection<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Integer ownerId, LocalDateTime now,
                                                                      LocalDateTime now1);

    Collection<Booking> findAllByItemOwnerIdAndEndBeforeOrderByIdDesc(Integer ownerId, LocalDateTime now);

    Collection<Booking> findAllByItemOwnerIdAndStartAfterOrderByIdDesc(Integer ownerId, LocalDateTime now);

    Collection<Booking> findByBookerIdAndItemIdAndStatusIsAndEndBefore(int bookerId, int itemId,
                                                                       BookingStatus status, LocalDateTime end);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.item.id = ?2 " +
            "and b.start < ?3 " +
            "and b.status = ?4 " +
            "order by b.start desc")
    List<Booking> findLastBooking(int userId, int itemId,
                                  LocalDateTime currentTime, BookingStatus status);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.item.id = ?2 " +
            "and b.start > ?3 " +
            "and b.status = ?4 " +
            "order by b.start")
    List<Booking> findNextBooking(int userId, int itemId,
                                  LocalDateTime currentTime, BookingStatus status);
}
