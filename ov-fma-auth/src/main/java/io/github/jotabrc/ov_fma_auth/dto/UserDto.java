package io.github.jotabrc.ov_fma_auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User Data Transfer Object.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDto extends UserDtoAbs {

    private String salt;
    private String hash;
    private boolean isActive;

    public UserDto(String uuid, String username, String email, String role, String salt, String hash, boolean isActive) {
        super(uuid, username, email, role);
        this.salt = salt;
        this.hash = hash;
        this.isActive = isActive;
    }
}
