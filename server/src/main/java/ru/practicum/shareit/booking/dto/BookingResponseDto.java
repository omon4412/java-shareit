package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponseDto {
    protected int id;
    protected LocalDateTime start;
    protected LocalDateTime end;
    protected Item item;
    protected User booker;
    protected BookingStatus status;
}
