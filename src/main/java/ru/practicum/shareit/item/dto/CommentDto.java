package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    protected int id;
    @NotNull(groups = Create.class)
    @Size(min = 10, groups = Create.class)
    protected String text;
    protected String authorName;
    protected LocalDateTime created;
}
