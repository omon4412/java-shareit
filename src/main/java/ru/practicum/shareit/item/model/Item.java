/**
 * Класс, представляющий вещь.
 */
package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
public class Item {

    /**
     * Идентификатор вещи.
     */
    protected int id;

    /**
     * Название вещи.
     */
    protected String name;

    /**
     * Описание вещи.
     */
    protected String description;

    /**
     * Признак доступности вещи.
     */
    protected Boolean available;

    /**
     * Владелец вещи.
     */
    protected User owner;
}
