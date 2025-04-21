package io.github.jotabrc.ov_fma_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User Data Transfer Object abstract class.
 * Exposes only necessary data.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserDtoAbs {

    private String uuid;
    private String username;
    private String email;
    private String role;
}
