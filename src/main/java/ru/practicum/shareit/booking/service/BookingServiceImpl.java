package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingBadRequestException;
import ru.practicum.shareit.booking.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.WrongOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public Booking save(int userId, Booking booking) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });

        int itemId = booking.getItem().getId();
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> {
                    log.error("Предмет с id=" + itemId + " не найден");
                    return new ItemNotFoundException("Предмет с id=" + itemId + " не найден");
                });

        if (item.getOwner().getId() == userId) {
            log.error("Нельзя забронировать предмет у самого себя");
            throw new WrongOwnerException("Нельзя забронировать предмет у самого себя");
        }
        if (!item.getAvailable()) {
            log.error("Предмет не доступен для бронирования");
            throw new ItemNotAvailableException("Предмет не доступен для бронирования");
        }
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().equals(booking.getEnd())) {
            log.error("Дата окончания брони не может быть раньше даты начала брони");
            throw new BookingBadRequestException("Дата окончания брони не может быть раньше даты начала брони");
        }
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveItem(int userId, int bookingId, boolean approved) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Бронь с id=" + bookingId + " не найдена");
                    return new BookingNotFoundException("Бронь с id=" + bookingId + " не найдена");
                });

        if (booking.getItem().getOwner().getId() != userId) {
            log.error("Пользователя с id=" + userId + "не может подтвердить бронь у не своего предмета");
            throw new WrongOwnerException("Нельзя забронировать предмет у самого себя");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
            return bookingRepository.save(booking);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            return bookingRepository.save(booking);
        }
    }
}
