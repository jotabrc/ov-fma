package io.github.jotabrc.ov_fma_user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.ov_fma_user.dto.UserCreationUpdateDto;
import io.github.jotabrc.ov_fma_user.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.security.NoSuchAlgorithmException;

@Validated
public sealed interface UserService permits UserServiceImpl {

    UserDto save(@NotNull UserCreationUpdateDto dto) throws NoSuchAlgorithmException, JsonProcessingException;
    UserDto update(@NotNull String userUuid, @NotNull UserCreationUpdateDto dto) throws NoSuchAlgorithmException, JsonProcessingException;
    UserDto get(@NotNull String userUuid);
}
