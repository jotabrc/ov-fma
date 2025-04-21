package io.github.jotabrc.ov_fma_auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SignIn Data Transfer Object.
 */
@Getter
@Setter
@NoArgsConstructor
public class SignInDto {

    private String username;
    private String password;
}
