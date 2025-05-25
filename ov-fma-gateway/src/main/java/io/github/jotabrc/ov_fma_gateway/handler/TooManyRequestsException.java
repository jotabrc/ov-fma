package io.github.jotabrc.ov_fma_gateway.handler;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}
