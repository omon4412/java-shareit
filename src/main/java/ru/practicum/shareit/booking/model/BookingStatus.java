package ru.practicum.shareit.booking.model;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    CANCELED,
    UNKNOWN;

    public static boolean contains(String test) {

        for (BookingStatus c : BookingStatus.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}

