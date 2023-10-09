package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.Create;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Контроллер для управления бронированием.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    /**
     * Сервис управления бронированием.
     */
    private final BookingService bookingService;

    /**
     * Создает новое бронирование.
     *
     * @param userId     идентификатор пользователя, создающего бронирование
     * @param bookingDto объект, содержащий информацию о бронировании
     * @return объект BookingResponseDto, представляющий созданное бронирование
     */
    @PostMapping
    public BookingResponseDto save(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Validated(Create.class) @RequestBody BookingDto bookingDto) {

        Booking booking = BookingMapper.toBooking(bookingDto);
        return BookingMapper.toBookingResponseDto(bookingService.save(userId, booking));
    }

    /**
     * Изменяет статус бронирования.
     *
     * @param userId    идентификатор пользователя, изменяющего статус бронирования
     * @param bookingId идентификатор бронирования, статус которого нужно изменить
     * @param approved  флаг, указывающий на одобрение или отклонение бронирования
     * @return объект BookingResponseDto, представляющий обновленное бронирование
     */
    @PatchMapping("/{bookingId}")
    public BookingResponseDto changeStatus(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable int bookingId, @RequestParam boolean approved) {
        Booking booking = bookingService.changeStatus(userId, bookingId, approved);
        return BookingMapper.toBookingResponseDto(booking);
    }

    /**
     * Получает информацию о бронировании по его идентификатору.
     *
     * @param userId    идентификатор пользователя, запрашивающего информацию о бронировании
     * @param bookingId идентификатор бронирования, информацию о котором нужно получить
     * @return объект BookingResponseDto, представляющий запрошенное бронирование
     */
    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable int bookingId) {
        Booking booking = bookingService.getById(userId, bookingId);
        return BookingMapper.toBookingResponseDto(booking);
    }

    /**
     * Получает список бронирований пользователя.
     *
     * @param userId идентификатор пользователя, для которого нужно получить список бронирований
     * @param state  фильтр статуса бронирования
     * @return коллекция объектов BookingResponseDto, представляющих список бронирований
     */
    @GetMapping
    public Collection<BookingResponseDto> getAll(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "ALL") String state) {

        BookingStatus bookingStatus;
        if (BookingStatus.contains(state)) {
            bookingStatus = BookingStatus.valueOf(state);
        } else {
            bookingStatus = BookingStatus.UNKNOWN;
        }
        Collection<Booking> bookings = bookingService.getAll(userId, bookingStatus);
        return bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает список бронирований владельца предмета.
     *
     * @param ownerId идентификатор владельца предмета, для которого нужно получить список бронирований.
     * @param state   фильтр статуса бронирования.
     * @return коллекция объектов BookingResponseDto, представляющих список бронирований.
     */
    @GetMapping("/owner")
    public Collection<BookingResponseDto> getAllByOwner(
            @RequestHeader("X-Sharer-User-Id") Integer ownerId,
            @RequestParam(defaultValue = "ALL") String state) {

        BookingStatus bookingStatus;
        if (BookingStatus.contains(state)) {
            bookingStatus = BookingStatus.valueOf(state);
        } else {
            bookingStatus = BookingStatus.UNKNOWN;
        }
        Collection<Booking> bookings = bookingService.getAllByOwner(ownerId, bookingStatus);
        return bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }
}
