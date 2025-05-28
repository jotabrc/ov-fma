package io.github.jotabrc.ov_fma_user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * User Data Transfer Object.
 */
@Getter
public final class UserDto extends UserDtoAbs implements Serializable {

    private final String uuid;
    private final RoleDto role;
    private final String name;

    @JsonCreator
    public UserDto(
            @JsonProperty("username") final String username,
            @JsonProperty("email") final String email,
            @JsonProperty("uuid") final String uuid,
            @JsonProperty("name") String name,
            @JsonProperty("role") final RoleDto role) {
        super(username, email);
        this.uuid = uuid;
        this.name = name;
        this.role = role;
    }
}
