package io.github.jotabrc.ov_fma_gateway.security;

import io.github.jotabrc.ovauth.jwt.TokenConfig;
import io.github.jotabrc.ovauth.jwt.TokenCreator;
import io.github.jotabrc.ovauth.jwt.TokenObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Component
public class CustomGlobalFilter implements GlobalFilter {

    /**
     * GlobalFilter applied to all requests, forward AUTHORIZATION header, creates and insert
     * X-Secure-Token to validate origin at services.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorization != null && !authorization.startsWith("Bearer")) authorization = null;

        String jwt = buildJwt();

        exchange = mutateRequest(exchange, authorization, jwt);
        return chain.filter(exchange);
    }

    /**
     * Build Gateway JWT.
     * @return JWT String.
     */
    private String buildJwt() {
        TokenObject tokenObject = TokenObject
                .builder()
                .subject("GATEWAY")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TokenConfig.EXPIRATION))
                .roles(List.of("SYSTEM"))
                .build();
        String jwt = TokenCreator.create(TokenConfig.PREFIX, TokenConfig.KEY, tokenObject);
        return jwt;
    }

    /**
     * Includes in the request the AUTHORIZATION and X-Secure-Token headers.
     * @param exchange
     * @param authorization
     * @param jwt
     * @return
     */
    private ServerWebExchange mutateRequest(ServerWebExchange exchange, String authorization, String jwt) {
        ServerHttpRequest request = exchange
                .getRequest()
                .mutate()
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .header("X-Secure-Token", jwt)
                .build();
        exchange = exchange.mutate().request(request).build();
        return exchange;
    }
}
