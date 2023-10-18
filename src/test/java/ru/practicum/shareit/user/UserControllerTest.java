package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;
    User user;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        user = User.builder()
                .id(1)
                .name("Test")
                .email("Test@test.test")
                .build();
        userDto = UserDto.builder()
                .id(1)
                .name("Test")
                .email("Test@test.test")
                .build();
    }

    @Test
    void testAddUser_whenAllCorrect_thenReturnUserDto() throws Exception {
        when(userService.addUser(any()))
                .thenReturn(user);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void testAddUser_whenNameIsNull_thenReturnBadRequest() throws Exception {
        UserDto badUser = UserDto.builder()
                .name(null)
                .email("Test@test.test")
                .build();
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(badUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddUser_whenEmailIsNull_thenReturnBadRequest() throws Exception {
        UserDto badUser = UserDto.builder()
                .name("Name")
                .email(null)
                .build();
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(badUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUser_whenAllCorrect_thenReturnUser() throws Exception {
        when(userService.getUser(anyInt()))
                .thenReturn(user);

        String result = mvc.perform(get("/users/{userId}", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(userDto), result);
    }

    @Test
    void testGetUser_whenIdIsWrong_thenReturnBadRequest() throws Exception {
        mvc.perform(get("/users/{userId}", "99991xvc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser() throws Exception {
        User user2 = User.builder()
                .id(1)
                .name("Test 2")
                .email("email")
                .build();
        when(userService.updateUser(any()))
                .thenReturn(user2);

        String result = mvc.perform(patch("/users/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(UserMapper.toUserDto(user)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(UserMapper.toUserDto(user2)), result);
    }

    @Test
    void deleteUser() throws Exception {
        when(userService.deleteUser(anyInt()))
                .thenReturn(user);
        String result = mvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(UserMapper.toUserDto(user)), result);
    }

    @Test
    void testGetAll_whenUsersIsEmpty_thenReturnEmptyList() throws Exception {
        when(userService.getAll())
                .thenReturn(Collections.emptyList());

        String result = mvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("[]", result);
    }

    @Test
    void testGetAll_whenOneUser_thenReturnListWithOneUser() throws Exception {
        when(userService.getAll())
                .thenReturn(List.of(user));

        String result = mvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(userDto)), result);
    }

    @Test
    void testGetAll_whenTwoUsers_thenReturnListWithTwoUsers() throws Exception {
        User user2 = User.builder()
                .id(2)
                .name("Test 2")
                .email("email")
                .build();
        when(userService.getAll())
                .thenReturn(List.of(user, user2));

        String result = mvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(userDto, UserMapper.toUserDto(user2))), result);
    }
}