package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Dto пользователя для добавления/обновления.
 */
@Data
@Builder
public class UserAddUpdateDto {
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
