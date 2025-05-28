package io.github.jotabrc.ov_fma_user.controller;

import io.github.jotabrc.ov_fma_user.dto.RoleDto;
import io.github.jotabrc.ov_fma_user.dto.UserCreationUpdateDto;
import io.github.jotabrc.ov_fma_user.dto.UserDto;
import io.github.jotabrc.ov_fma_user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void save() throws Exception {
        UserCreationUpdateDto dto = new UserCreationUpdateDto(
                "username",
                "email@email.com",
                "John Doe",
                "password1234"
        );
        RoleDto roleDto = new RoleDto(
                UUID.randomUUID().toString(),
                "USER",
                "USER Role"
        );
        UserDto userDto = new UserDto(
                UUID.randomUUID().toString(),
                "username",
                "email@gmail.com",
                "John Doe",
                roleDto
        );
        String json = """
                {
                    "username": "username",
                    "email": "email@email.com",
                    "name": "John Doe",
                    "password": "password1234"
                }
                """;
        when(userService.save(any())).thenReturn(userDto);
        mockMvc.perform(post("/api/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("User signup was successful");
                })
                .andDo(print());
    }

    @Test
    void update() throws Exception {
        UserCreationUpdateDto dto = new UserCreationUpdateDto(
                "username",
                "email@email.com",
                "John Doe",
                "password1234"
        );
        RoleDto roleDto = new RoleDto(
                UUID.randomUUID().toString(),
                "USER",
                "USER Role"
        );
        UserDto userDto = new UserDto(
                UUID.randomUUID().toString(),
                "username",
                "email@gmail.com",
                "John Doe",
                roleDto
        );
        String json = """
                {
                    "username": "newusername",
                    "email": "newemail@email.com",
                    "name": "John Bloggs",
                    "password": "password12345"
                }
                """;
        when(userService.update("user-uuid", dto)).thenReturn(userDto);
        mockMvc.perform(put("/api/v1/user/update/user-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("User update was successful");
                })
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        RoleDto roleDto = new RoleDto(
                UUID.randomUUID().toString(),
                "USER",
                "USER Role"
        );
        UserDto userDto = new UserDto(
                UUID.randomUUID().toString(),
                "username",
                "email@gmail.com",
                "John Doe",
                roleDto
        );
        when(userService.get("user-uuid")).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/get/user-uuid"))
                .andExpect(status().isFound())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains(userDto.getEmail());
                    assert response.contains(userDto.getUuid());
                })
                .andDo(print());
    }
}