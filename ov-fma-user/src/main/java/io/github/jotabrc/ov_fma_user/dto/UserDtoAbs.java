package io.github.jotabrc.ov_fma_user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.jotabrc.ov_fma_user.validation.ValidEmail;
import io.github.jotabrc.ov_fma_user.validation.ValidUsername;
import lombok.Getter;

import java.io.Serializable;

/**
 * User Data Transfer Object abstract class.
 * Exposes only necessary data.
 */
@Getter
public abstract sealed class UserDtoAbs implements Serializable permits UserCreationUpdateDto, UserDto {

    @ValidUsername
    private final String username;
    @ValidEmail
    private final String email;

    @JsonCreator
    public UserDtoAbs(
            @JsonProperty("username") String username,
            @JsonProperty("email") String email) {
        this.username = username;
        this.email = email;
    }
}
