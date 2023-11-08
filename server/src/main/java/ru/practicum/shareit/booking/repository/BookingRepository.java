package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findAllByBookerIdOrderByIdDesc(int bookerId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(
            int bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByIdDesc(int bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfterOrderByIdDesc(Integer userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusIs(Integer userId, BookingStatus state, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdOrderByIdDesc(int ownerId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatusIs(Integer ownerId, BookingStatus bookingStatus, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Integer ownerId, LocalDateTime now,
                                                                LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndEndBeforeOrderByIdDesc(Integer ownerId, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartAfterOrderByIdDesc(Integer ownerId, LocalDateTime now, Pageable pageable);

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
