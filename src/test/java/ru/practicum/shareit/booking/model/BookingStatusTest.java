package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingStatusTest {
    @Test
    void testContains() {
        assertTrue(BookingStatus.contains("canceled"));
        assertTrue(BookingStatus.contains("past"));
        assertFalse(BookingStatus.contains("dfgsdfsdfd"));
    }
}