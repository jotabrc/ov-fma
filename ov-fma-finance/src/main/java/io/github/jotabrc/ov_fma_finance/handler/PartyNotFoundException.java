package io.github.jotabrc.ov_fma_finance.handler;

public class PartyNotFoundException extends RuntimeException {
    public PartyNotFoundException(String message) {
        super(message);
    }
}
