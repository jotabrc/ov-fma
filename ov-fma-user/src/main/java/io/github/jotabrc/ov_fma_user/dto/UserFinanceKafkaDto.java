package io.github.jotabrc.ov_fma_user.dto;

import lombok.Getter;

/**
 * User Data Transfer Object.
 */
@Getter
public final class UserFinanceKafkaDto {

    private final String userUuid;
    private final String name;

    public UserFinanceKafkaDto(String userUuid,
                               String name) {
        this.userUuid = userUuid;
        this.name = name;
    }
}
