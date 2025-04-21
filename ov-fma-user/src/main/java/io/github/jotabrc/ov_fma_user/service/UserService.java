package io.github.jotabrc.ov_fma_user.service;

import io.github.jotabrc.ov_fma_user.dto.UserCreationUpdateDto;
import io.github.jotabrc.ov_fma_user.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.security.NoSuchAlgorithmException;

@Validated
public interface UserService {

    String signup(@NotNull UserCreationUpdateDto dto) throws NoSuchAlgorithmException;
    void update(@NotNull UserCreationUpdateDto dto) throws NoSuchAlgorithmException;

    UserDto getByUuid(@NotNull String uuid);
}
