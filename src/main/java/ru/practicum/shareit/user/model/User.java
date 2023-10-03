package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Модель пользователя.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    /**
     * Идентификатор пользователя.
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    /**
     * Имя пользователя.
     */
    @Column(name = "name", nullable = false)
    protected String name;

    /**
     * Электронная почта пользователя.
     */
    @Column(name = "email", nullable = false)
    protected String email;
}
