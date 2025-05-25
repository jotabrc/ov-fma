package io.github.jotabrc.ov_fma_user.dto;

import lombok.Getter;

/**
 * User Data Transfer Object.
 */
@Getter
public final class UserAuthKafkaDto {

    private final String userUuid;
    private final String username;
    private final String email;
    private final String role;
    private final String salt;
    private final String hash;
    private final boolean isActive;

    public UserAuthKafkaDto(String userUuid,
                            String username,
                            String email,
                            String role,
                            String salt,
                            String hash,
                            boolean isActive) {
        this.userUuid = userUuid;
        this.username = username;
        this.email = email;
        this.role = role;
        this.salt = salt;
        this.hash = hash;
        this.isActive = isActive;
    }
}
