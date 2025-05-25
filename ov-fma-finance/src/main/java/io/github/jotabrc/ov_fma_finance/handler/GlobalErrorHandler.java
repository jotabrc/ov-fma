package io.github.jotabrc.ov_fma_finance.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler({
            ReceiptNotFoundException.class,
            PartyNotFoundException.class,
            UserNotFoundException.class,
            PaymentNotFoundException.class
    })
    public ResponseEntity<String> handle(ReceiptNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handle(UserAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handle(UnauthorizedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handle(Throwable throwable) {
        return new ResponseEntity<>(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
