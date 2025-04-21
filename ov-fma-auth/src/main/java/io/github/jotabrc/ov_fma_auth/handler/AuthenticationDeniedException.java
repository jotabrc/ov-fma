package io.github.jotabrc.ov_fma_auth.handler;

public class AuthenticationDeniedException extends RuntimeException {
    public AuthenticationDeniedException(String message) {
        super(message);
    }
}
