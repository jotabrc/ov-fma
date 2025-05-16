package io.github.jotabrc.ov_fma_user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * User Data Transfer Object abstract class.
 * Exposes only necessary data.
 */
@Getter
public abstract class UserDtoAbs implements Serializable {

    private final String uuid;
    private final String username;
    private final String email;

    @JsonCreator
    public UserDtoAbs(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
    }
}
