package io.github.jotabrc.ov_fma_user.dto;

import lombok.Getter;

/**
 * User Data Transfer Object.
 */
@Getter
public class UserDto extends UserDtoAbs {

    private final RoleDto role;
    private final String name;

    public UserDto(final String uuid, final String username, final String email, String name, final RoleDto role) {
        super(uuid, username, email);
        this.name = name;
        this.role = role;
    }
}
