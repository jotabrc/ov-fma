package io.github.jotabrc.ov_fma_user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * Role Data Transfer Object.
 * Exposes only necessary data.
 */
@Getter
public class RoleDto implements Serializable {

    private final String uuid;
    private final String name;
    private final String description;

    @JsonCreator
    public RoleDto(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
    }
}
