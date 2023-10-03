package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * Утилитарный класс для преобразования объектов типа User и связанных DTO.
 * Этот класс предоставляет методы для конвертации между сущностью User и DTO
 * {@link UserDto} и {@link User}
 */
public class UserMapper {
    /**
     * Преобразует объект типа UserDto в объект типа User.
     *
     * @param userDto Объект типа UserGetDto для преобразования
     * @return Объект типа User
     */
    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    /**
     * Преобразует объект типа User в объект типа UserDto.
     *
     * @param user Объект типа User для преобразования
     * @return Объект типа UserDto
     */
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
