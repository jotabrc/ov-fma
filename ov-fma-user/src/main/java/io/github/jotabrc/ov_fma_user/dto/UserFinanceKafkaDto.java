package io.github.jotabrc.ov_fma_user.dto;

import lombok.Getter;

/**
 * User Data Transfer Object.
 */
@Getter
public class UserFinanceKafkaDto extends UserDtoAbs {

    private final boolean isActive;

    public UserFinanceKafkaDto(final String uuid, final String username, final String email, boolean isActive) {
        super(uuid, username, email);
        this.isActive = isActive;
    }
}
