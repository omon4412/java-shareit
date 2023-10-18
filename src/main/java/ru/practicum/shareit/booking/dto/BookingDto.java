package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    protected int id;

    @NotNull(groups = {Create.class})
    @FutureOrPresent(groups = {Create.class})
    protected LocalDateTime start;

    @NotNull(groups = {Create.class})
    @Future(groups = {Create.class})
    protected LocalDateTime end;

    @NotNull(groups = {Create.class})
    protected Integer itemId;
}
