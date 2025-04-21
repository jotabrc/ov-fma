package io.github.jotabrc.ov_fma_user.handler;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
