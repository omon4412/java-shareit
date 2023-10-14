package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

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
            throw new BookingNotFoundException("Нельзя забронировать предмет у самого себя");
        }
        if (!item.getAvailable()) {
            log.error("Предмет недоступен для бронирования");
            throw new ItemNotAvailableException("Предмет недоступен для бронирования");
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

    @Transactional
    @Override
    public Booking changeStatus(int userId, int bookingId, boolean approved) {
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
            throw new BookingNotFoundException("Нельзя забронировать предмет у самого себя");
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new BookingBadRequestException("Бронь уже подтверждена или отклонена");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public Booking getById(Integer userId, int bookingId) {
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

        if (!(booking.getItem().getOwner().getId() == userId) && !(booking.getBooker().getId() == userId)) {
            log.error("Бронь с id=" + bookingId + " не найдена");
            throw new BookingNotFoundException("Бронь с id=" + bookingId + " не найдена");
        }

        return booking;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Booking> getAll(Integer userId, BookingStatus state, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });

        PageRequest pageRequest = PageRequest.of(from / size, size);

        switch (state) {
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusIs(userId, BookingStatus.WAITING, pageRequest)
                        .getContent();
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusIs(userId, BookingStatus.REJECTED, pageRequest)
                        .getContent();
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByIdDesc(userId, pageRequest).getContent();
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now(), pageRequest).getContent();
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByIdDesc(
                        userId, LocalDateTime.now(), pageRequest).getContent();
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByIdDesc(userId, LocalDateTime.now(),
                        pageRequest).getContent();
            default:
                throw new BookingBadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Booking> getAllByOwner(Integer ownerId, BookingStatus bookingStatus,
                                             Integer from, Integer size) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + ownerId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + ownerId + " не найден");
                });

        PageRequest pageRequest = PageRequest.of(from / size, size);

        switch (bookingStatus) {
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusIs(ownerId, BookingStatus.WAITING, pageRequest)
                        .getContent();
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusIs(ownerId, BookingStatus.REJECTED, pageRequest)
                        .getContent();
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByIdDesc(ownerId, pageRequest).getContent();
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(
                        ownerId, LocalDateTime.now(), LocalDateTime.now(), pageRequest).getContent();
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByIdDesc(ownerId, LocalDateTime.now(),
                        pageRequest).getContent();
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByIdDesc(ownerId, LocalDateTime.now(),
                        pageRequest).getContent();
            default:
                throw new BookingBadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
