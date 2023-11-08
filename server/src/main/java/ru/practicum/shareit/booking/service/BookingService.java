package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Collection;

/**
 * Интерфейс, предоставляющий методы для работы с бронированиями.
 */
public interface BookingService {
    /**
     * Сохраняет новое бронирование.
     *
     * @param userId  идентификатор пользователя, совершившего бронирование
     * @param booking объект бронирования, который необходимо сохранить
     * @return сохраненное бронирование
     */
    Booking save(int userId, Booking booking);

    /**
     * Изменяет статус бронирования.
     *
     * @param userId    идентификатор пользователя, меняющего статус бронирования
     * @param bookingId идентификатор бронирования, статус которого необходимо изменить
     * @param approved  флаг одобрения бронирования
     * @return бронирование с измененным статусом
     */
    Booking changeStatus(int userId, int bookingId, boolean approved);

    /**
     * Получает бронирование по его идентификатору.
     *
     * @param userId    идентификатор пользователя, запрашивающего бронирование
     * @param bookingId идентификатор бронирования
     * @return бронирование с указанным идентификатором
     */
    Booking getById(Integer userId, int bookingId);

    /**
     * Получает все бронирования пользователя с заданным статусом.
     *
     * @param userId идентификатор пользователя, чьи бронирования необходимо получить
     * @param state  статус бронирования
     * @param from   индекс первого элемента, начиная с 0
     * @param size   количество элементов для отображения
     * @return коллекция бронирований пользователя с указанным статусом
     */
    Collection<Booking> getAll(Integer userId, BookingStatus state, Integer from, Integer size);

    /**
     * Получает все бронирования объектов владельцы с заданным статусом.
     *
     * @param ownerId       идентификатор владельца объектов
     * @param bookingStatus статус бронирования
     * @param from          индекс первого элемента, начиная с 0
     * @param size          количество элементов для отображения
     * @return коллекция бронирований объектов владельца с указанным статусом
     */
    Collection<Booking> getAllByOwner(Integer ownerId, BookingStatus bookingStatus, Integer from, Integer size);
}
