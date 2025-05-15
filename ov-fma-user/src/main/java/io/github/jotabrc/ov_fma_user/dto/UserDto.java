package io.github.jotabrc.ov_fma_user.dto;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * User Data Transfer Object.
 */
@Getter
@RedisHash(value = "UserDto", timeToLive = 3600)
public class UserDto extends UserDtoAbs implements Serializable {

    private final RoleDto role;
    private final String name;

    public UserDto(final String uuid, final String username, final String email, String name, final RoleDto role) {
        super(uuid, username, email);
        this.name = name;
        this.role = role;
    }
}
