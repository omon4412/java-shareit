package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO для представления данных о предмете.
 */
@Data
@Builder
public class ItemDto {

    /**
     * Идентификатор предмета.
     */
    protected int id;

    /**
     * Название предмета.
     */
    @NotBlank(groups = {Create.class})
    protected String name;

    /**
     * Описание предмета.
     */
    @NotBlank(groups = {Create.class})
    protected String description;

    /**
     * Признак доступности предмета.
     */
    @NotNull(groups = {Create.class})
    protected Boolean available;
}
