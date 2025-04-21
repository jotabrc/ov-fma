package io.github.jotabrc.ov_fma_user.handler;

public class CredentialNotAvailableException extends RuntimeException {
    public CredentialNotAvailableException(String message) {
        super(message);
    }
}
