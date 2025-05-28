package io.github.jotabrc.ov_fma_user.dto;

import io.github.jotabrc.ov_fma_user.validation.ValidName;
import io.github.jotabrc.ov_fma_user.validation.ValidPassword;
import lombok.Getter;

/**
 * User Creation Data Transfer Object.
 */
@Getter
public final class UserCreationUpdateDto extends UserDtoAbs {

    @ValidPassword
    private final String password;

    @ValidName
    private final String name;

    public UserCreationUpdateDto(final String username, final String email, final String name, final String password) {
        super(username, email);
        this.name = name;
        this.password = password;
    }
}
