package io.github.jotabrc.ov_fma_gateway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
public class GlobalExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        String exception = ex.getClass().getSimpleName();
        HttpStatus status = null;

        switch (exception) {
            case "TooManyRequestsException" -> status = HttpStatus.TOO_MANY_REQUESTS;
            default -> status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        exchange.getResponse().setStatusCode(status);
        return exchange
                .getResponse()
                .writeWith(
                        Mono
                                .just(
                                        exchange
                                                .getResponse()
                                                .bufferFactory()
                                                .wrap(("Error: " + ex.getMessage())
                                                        .getBytes())));
    }
}
