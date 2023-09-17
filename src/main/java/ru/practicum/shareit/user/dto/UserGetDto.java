package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Dto пользователя для передачи.
 */
@Data
@Builder
public class UserGetDto {
    /**
     * Идентификатор пользователя.
     */
    protected int id;

    /**
     * Имя пользователя.
     */
    @NotBlank
    protected String name;

    /**
     * Электронная почта пользователя.
     */
    @NotBlank
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]" +
            "+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    protected String email;
}
