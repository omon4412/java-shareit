package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    protected int id;

    @NotBlank(groups = {Create.class})
    protected String name;

    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class, Update.class},
            regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]" +
                    "+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    protected String email;
}
