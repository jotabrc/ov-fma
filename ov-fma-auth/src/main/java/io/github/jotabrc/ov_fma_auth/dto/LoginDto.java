package io.github.jotabrc.ov_fma_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SignIn Data Transfer Object.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class LoginDto {

    private String username;
    private String password;
}
