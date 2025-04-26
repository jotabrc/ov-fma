package io.github.jotabrc.ov_fma_finance.dto;

import lombok.Getter;

/**
 * User Creation Data Transfer Object.
 */
@Getter
public class UserCreationUpdateDto extends UserDtoAbs {

    private final String password;
    private final String name;

    public UserCreationUpdateDto(final String uuid, final String username, final String email, final String name, final String password) {
        super(uuid, username, email);
        this.name = name;
        this.password = password;
    }
}
