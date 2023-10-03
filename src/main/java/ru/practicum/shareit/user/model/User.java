package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

/**
 * Модель пользователя.
 */
@Data
@Builder
public class User {
    /**
     * Идентификатор пользователя.
     */
    protected int id;

    /**
     * Имя пользователя.
     */
    protected String name;

    /**
     * Электронная почта пользователя.
     */
    protected String email;
}
