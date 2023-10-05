package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.Create;

import java.util.Collection;
import java.util.stream.Collectors;

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
    public BookingResponseDto changeStatus(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable int bookingId, @RequestParam boolean approved) {
        Booking booking = bookingService.changeStatus(userId, bookingId, approved);
        return BookingMapper.toBookingResponseDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable int bookingId) {
        Booking booking = bookingService.getById(userId, bookingId);
        return BookingMapper.toBookingResponseDto(booking);
    }

    @GetMapping
    public Collection<BookingResponseDto> getAll(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "ALL") String state) {

        BookingStatus bookingStatus;
        if(BookingStatus.contains(state)){
            bookingStatus = BookingStatus.valueOf(state);
        }
        else {
            bookingStatus = BookingStatus.UNKNOWN;
        }
        Collection<Booking> bookings = bookingService.getAll(userId, bookingStatus);
        return bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public Collection<BookingResponseDto> getAllByOwner(
            @RequestHeader("X-Sharer-User-Id") Integer ownerId,
            @RequestParam(defaultValue = "ALL") String state) {

        BookingStatus bookingStatus;
        if(BookingStatus.contains(state)){
            bookingStatus = BookingStatus.valueOf(state);
        }
        else {
            bookingStatus = BookingStatus.UNKNOWN;
        }
        Collection<Booking> bookings = bookingService.getAllByOwner(ownerId, bookingStatus);
        return bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }
}
