package io.github.jotabrc.ov_fma_user.dto;

import lombok.Getter;

/**
 * User Creation Data Transfer Object.
 */
@Getter
public class UserCreationUpdateDto extends UserDtoAbs {

    private final String password;

    public UserCreationUpdateDto(final String uuid, final String username, final String email, final String name, final String password) {
        super(uuid, username, email, name);
        this.password = password;
    }
}
