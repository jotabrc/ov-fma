package io.github.jotabrc.ov_fma_auth.service;

import io.github.jotabrc.ov_fma_auth.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AuthService {

    void add(@NotNull UserDto dto);
    void update(@NotNull UserDto dto);
}
