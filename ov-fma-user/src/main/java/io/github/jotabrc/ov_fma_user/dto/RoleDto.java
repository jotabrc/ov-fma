package io.github.jotabrc.ov_fma_user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Role Data Transfer Object.
 * Exposes only necessary data.
 */
@Getter
@AllArgsConstructor
public class RoleDto {

    private final String uuid;
    private final String name;
    private final String description;
}
