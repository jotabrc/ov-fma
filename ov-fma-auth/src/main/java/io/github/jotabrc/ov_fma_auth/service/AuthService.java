package io.github.jotabrc.ov_fma_auth.service;

import io.github.jotabrc.ov_fma_auth.dto.LoginDto;
import io.github.jotabrc.ov_fma_auth.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.security.NoSuchAlgorithmException;

@Validated
public sealed interface AuthService permits AuthServiceImpl {

    void save(@NotNull UserDto dto);
    void update(@NotNull UserDto dto);
    String login(@NotNull String userUuid, @NotNull LoginDto dto) throws NoSuchAlgorithmException;
}
