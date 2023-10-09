package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

/**
 * Класс-маппер для преобразования между объектами Booking и BookingDto
 */
public class BookingMapper {
    /**
     * Преобразует объект Booking в BookingDto.
     *
     * @param booking объект Booking, который необходимо преобразовать
     * @return объект BookingDto
     */
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .build();
    }

    /**
     * Преобразует объект BookingDto в Booking.
     *
     * @param bookingDto объект BookingDto, который необходимо преобразовать
     * @return объект Booking
     */
    public static Booking toBooking(BookingDto bookingDto) {
        Item itemOnlyWithId = new Item();
        itemOnlyWithId.setId(bookingDto.getItemId());
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(itemOnlyWithId)
                .build();
    }

    /**
     * Преобразует объект Booking в BookingResponseDto.
     *
     * @param booking объект Booking, который необходимо преобразовать
     * @return объект BookingResponseDto
     */
    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .item(booking.getItem())
                .build();
    }
}
