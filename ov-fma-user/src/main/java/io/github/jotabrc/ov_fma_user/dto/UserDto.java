package io.github.jotabrc.ov_fma_user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * User Data Transfer Object.
 */
@Getter
public class UserDto extends UserDtoAbs implements Serializable {

    private final RoleDto role;
    private final String name;

    @JsonCreator
    public UserDto(
            @JsonProperty("uuid") final String uuid,
            @JsonProperty("username") final String username,
            @JsonProperty("email") final String email,
            @JsonProperty("name") String name,
            @JsonProperty("role") final RoleDto role) {
        super(uuid, username, email);
        this.name = name;
        this.role = role;
    }
}
