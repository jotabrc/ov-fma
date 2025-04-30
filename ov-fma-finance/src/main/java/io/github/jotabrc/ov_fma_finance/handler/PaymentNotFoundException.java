package io.github.jotabrc.ov_fma_finance.handler;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
