package io.github.jotabrc.ov_fma_user.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handle(AuthorizationDeniedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            RoleNotFoundException.class
    })
    public ResponseEntity<String> handle(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CredentialNotAvailableException.class)
    public ResponseEntity<String> handle(CredentialNotAvailableException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handle(Throwable throwable) {
        return new ResponseEntity<>(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
