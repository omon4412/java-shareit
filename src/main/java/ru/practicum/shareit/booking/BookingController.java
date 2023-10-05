package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.Create;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto save(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Validated(Create.class) @RequestBody BookingDto bookingDto) {

        Booking booking = BookingMapper.toBooking(bookingDto);
        return BookingMapper.toBookingResponseDto(bookingService.save(userId, booking));
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto save(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable int bookingId, @RequestParam boolean approved) {
        Booking booking = bookingService.approveItem(userId, bookingId, approved);
        return BookingMapper.toBookingResponseDto(booking);
    }
}
