package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookingsByStatus(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        BookingStatus status = checkStatus(stateText);
        return bookingClient.getBookingsByStatus(userId, status, from, size);
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> getBookingsByOwnerAndStatus(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        BookingStatus status = checkStatus(stateText);
        return bookingClient.getBookingsByOwnerAndStatus(userId, status, from, size);
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> getBooking(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Validated(Create.class)
            @RequestBody BookItemRequestDto bookingRequestDto) {
        return bookingClient.createBooking(userId, bookingRequestDto);
    }


    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer bookingId,
            @RequestParam Boolean approved) {

        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }

    @DeleteMapping(value = "/{bookingId}")
    public void deleteBooking(@PathVariable Integer bookingId) {
        bookingClient.deleteBooking(bookingId);
    }

    private BookingStatus checkStatus(String stateText) {
        if (BookingStatus.contains(stateText)) {
            return BookingStatus.valueOf(stateText);
        } else {
            return BookingStatus.UNKNOWN;
        }
    }
}
