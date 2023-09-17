package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO для представления данных о вещи.
 */
@Data
@Builder
public class ItemDto {

    /**
     * Идентификатор вещи.
     */
    protected int id;

    /**
     * Название вещи.
     */
    @NotBlank
    protected String name;

    /**
     * Описание вещи.
     */
    @NotBlank
    protected String description;

    /**
     * Признак доступности вещи.
     */
    @NotNull
    protected Boolean available;
}
