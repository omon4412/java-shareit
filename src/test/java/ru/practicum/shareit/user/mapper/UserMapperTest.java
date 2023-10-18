package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    User user = User.builder()
            .id(1)
            .email("user@user.user")
            .name("user")
            .build();
    UserDto userDto = UserDto.builder()
            .email("userDto@userDto.userDto")
            .name("userDto")
            .id(2)
            .build();

    @Test
    void toUser() {
        User userTest = UserMapper.toUser(userDto);
        assertEquals(2, userTest.getId());
        assertEquals("userDto@userDto.userDto", userTest.getEmail());
        assertEquals("userDto", userTest.getName());
    }

    @Test
    void toUserDto() {
        UserDto userDtoTest = UserMapper.toUserDto(user);
        assertEquals(1, userDtoTest.getId());
        assertEquals("user@user.user", userDtoTest.getEmail());
        assertEquals("user", userDtoTest.getName());
    }
}