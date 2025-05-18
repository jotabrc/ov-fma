package io.github.jotabrc.ov_fma_auth.service;

import io.github.jotabrc.ov_fma_auth.dto.SignInDto;
import io.github.jotabrc.ov_fma_auth.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.security.NoSuchAlgorithmException;

@Validated
public interface AuthService {

    void add(@NotNull UserDto dto);
    void update(@NotNull UserDto dto);

    String signIn(@NotNull SignInDto dto, @NotNull String uuid) throws NoSuchAlgorithmException;
}
