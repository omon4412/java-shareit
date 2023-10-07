package ru.practicum.shareit.booking.model;

import java.util.Arrays;

/**
 * Перечисление, представляющее статус бронирования.
 */
public enum BookingStatus {
    /**
     * Ожидание подтверждения.
     */
    WAITING,

    /**
     * Бронирование одобрено.
     */
    APPROVED,

    /**
     * Бронирование отклонено.
     */
    REJECTED,

    /**
     * Все статусы.
     */
    ALL,

    /**
     * Текущее бронирование.
     */
    CURRENT,

    /**
     * Завершённое бронирование.
     */
    PAST,

    /**
     * Будущее бронирование.
     */
    FUTURE,

    /**
     * Бронирование отменено.
     */
    CANCELED,

    /**
     * Неизвестный статус.
     */
    UNKNOWN;

    /**
     * Проверяет, содержит ли перечисление указанное имя статуса.
     *
     * @param statusName Имя статуса для проверки.
     * @return true, если перечисление содержит статус с указанным именем, в противном случае false.
     */
    public static boolean contains(String statusName) {
        return Arrays.stream(BookingStatus.values())
                .anyMatch(c -> statusName.equalsIgnoreCase(c.name()));
    }
}
