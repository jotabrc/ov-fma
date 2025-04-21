package io.github.jotabrc.ov_fma_user.dto;

import lombok.Getter;

/**
 * User Data Transfer Object.
 */
@Getter
public class UserKafkaDto extends UserDtoAbs {

    private final String role;
    private final String salt;
    private final String hash;
    private final boolean isActive;

    public UserKafkaDto(final String uuid, final String username, final String email, final String role, String salt, String hash, boolean isActive) {
        super(uuid, username, email);
        this.role = role;
        this.salt = salt;
        this.hash = hash;
        this.isActive = isActive;
    }
}
