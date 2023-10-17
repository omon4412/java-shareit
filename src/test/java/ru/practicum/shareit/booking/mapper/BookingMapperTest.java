package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    @Test
    void testToBookingDto() {
        Booking booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .item(new Item())
                .build();

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItemId());
    }

    @Test
    void testToBooking() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .itemId(2)
                .build();

        Booking booking = BookingMapper.toBooking(bookingDto);

        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getStart(), booking.getStart());
        assertEquals(bookingDto.getEnd(), booking.getEnd());
        assertEquals(bookingDto.getItemId(), booking.getItem().getId());
    }

    @Test
    void testToBookingResponseDto() {
        Booking booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .booker(new User())
                .status(BookingStatus.APPROVED)
                .item(new Item())
                .build();

        BookingResponseDto bookingResponseDto = BookingMapper.toBookingResponseDto(booking);

        assertEquals(booking.getId(), bookingResponseDto.getId());
        assertEquals(booking.getStart(), bookingResponseDto.getStart());
        assertEquals(booking.getEnd(), bookingResponseDto.getEnd());
        assertEquals(booking.getBooker(), bookingResponseDto.getBooker());
        assertEquals(booking.getStatus(), bookingResponseDto.getStatus());
        assertEquals(booking.getItem(), bookingResponseDto.getItem());
    }

}