package io.github.jotabrc.ov_fma_user.dto;

import lombok.Getter;

/**
 * User Data Transfer Object.
 */
@Getter
public class UserDto extends UserDtoAbs {

    private final RoleDto role;

    public UserDto(final String uuid, final String username, final String email, final String name, final RoleDto role) {
        super(uuid, username, email, name);
        this.role = role;
    }
}
