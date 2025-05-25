package io.github.jotabrc.ov_fma_gateway.config;

import io.github.jotabrc.ov_fma_gateway.handler.TooManyRequestsException;
import io.github.jotabrc.ovauth.jwt.TokenConfig;
import io.github.jotabrc.ovauth.jwt.TokenCreator;
import io.github.jotabrc.ovauth.jwt.TokenObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.SignatureException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class RateLimiting implements GlobalFilter {

    private final RedisConfig redisConfig;

    @Value("${ov.fma.gateway.requests.rate-limit}")
    private Long rateLimit;

    @Value("${ov.fma.gateway.requests.rate-limit-authenticated}")
    private Long rateLimitAuthenticated;

    @Value("${ov.fma.gateway.requests.cache-duration}")
    private Long cacheDuration;

    public RateLimiting(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    /**
     * Creates Cache with JWT User UUID or User IP address for rate limiting
     * @param exchange the current server exchange
     * @param chain provides a way to delegate to the next filter
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        Optional<String> cacheKey;
        AtomicLong rateLimitSet = new AtomicLong(this.rateLimit);
        if (header != null && header.startsWith("Bearer")) {
            cacheKey = getJwt(header);
            rateLimitSet.set(this.rateLimitAuthenticated);
        } else {
            cacheKey = getUserIp(exchange);
        }

        cacheKey.ifPresent(k -> doCache(k, rateLimitSet.get()));
        return chain.filter(exchange);
    }

    /**
     * Check user requests rate if present, otherwise initialize cache with JWT User UUID, or request User IP.
     * @param cacheKey Key for Cache.
     */
    private void doCache(final String cacheKey, final long rateLimit) {
        String key = "rate-limiting:" + cacheKey;
        Boolean firstAttempt = redisConfig.redisTemplate().opsForValue().setIfAbsent(key, 1, Duration.ofMinutes(cacheDuration));
        Long tries = 1L;
        if (Boolean.FALSE.equals(firstAttempt)) tries = redisConfig.redisTemplate().opsForValue().increment(key, 1);
        if (tries != null && tries.compareTo(rateLimit) >= 0) throw new TooManyRequestsException("Rate limit exceeded");
    }

    /**
     * Retrieves User IP with X-Forwarded-For or with Remote Address / HostAddress
     * @param exchange Request.
     * @return Return Cache Key.
     */
    private Optional<String> getUserIp(ServerWebExchange exchange) {
        Optional<String> cacheKey;
        cacheKey = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"));
        if (cacheKey.isEmpty()) {
            var remoteAddress = exchange.getRequest().getRemoteAddress();
            if (remoteAddress != null)
                cacheKey = Optional.of(remoteAddress.getAddress().getHostAddress());
        }
        return cacheKey;
    }

    /**
     * Get JWT from Authorization Header.
     * @param header Authorization header.
     * @return User UUID wrapped in Optional of String
     */
    private Optional<String> getJwt(final String header) {
        Optional<String> cacheKey;
        try {
            TokenObject jwt = TokenCreator.decode(header, TokenConfig.PREFIX, TokenConfig.KEY);
            cacheKey = Optional.of(jwt.getSubject());

        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        return cacheKey;
    }
}
