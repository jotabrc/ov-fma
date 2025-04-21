package io.github.jotabrc.ov_fma_user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * User Data Transfer Object abstract class.
 * Exposes only necessary data.
 */
@Getter
@AllArgsConstructor
public abstract class UserDtoAbs {

    private final String uuid;
    private final String username;
    private final String email;
    private final String name;
}
