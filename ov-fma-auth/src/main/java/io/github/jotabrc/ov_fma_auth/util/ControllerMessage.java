package io.github.jotabrc.ov_fma_auth.util;

import lombok.Builder;

import java.util.StringJoiner;

@Builder
public record ControllerMessage(String message, String uuid) {

    public String toJSON() {
        return new StringJoiner(", ", "{", "}")
                .add("\"message\":\"" + message + "\"")
                .add("\"uuid\":\"" + uuid + "\"")
                .toString();
    }
}
