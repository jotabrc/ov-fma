package io.github.jotabrc.ov_fma_gateway.config;

import io.github.jotabrc.ovauth.config.PropertiesWhitelistLoaderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class GatewayConfig implements WebFluxConfigurer {

    private final LoadProperties loadProperties;
    private final String[] WHITELIST;

    @Autowired
    public GatewayConfig(LoadProperties loadProperties) {
        this.loadProperties = loadProperties;
        this.WHITELIST = PropertiesWhitelistLoaderImpl.WHITELIST.values().toArray(new String[0]);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder locatorBuilder) {
        return locatorBuilder.routes()

                // === USER ===
                .route(RouteConfig.routeConfig.get("userServiceName"), r -> r.path(RouteConfig.routeConfig.get("userServicePath"))
                        .filters(f -> f
                                .rewritePath(RouteConfig.routeConfig.get("userServicePattern"), RouteConfig.routeConfig.get("userServiceReplacement")))
                        .uri(RouteConfig.routeConfig.get("userServiceUri"))
                )

                // == H2 USER ===
                .route(RouteConfig.routeConfig.get("userServiceH2Name"), r -> r.path(RouteConfig.routeConfig.get("userServiceH2Path"))
                        .filters(f -> f
                                .rewritePath(RouteConfig.routeConfig.get("userServiceH2Pattern"), RouteConfig.routeConfig.get("userServiceH2Replacement")))
                        .uri(RouteConfig.routeConfig.get("userServiceUri"))
                )

                // === SWAGGER USER ===
                .route(RouteConfig.routeConfig.get("userServiceSwaggerName"), r -> r.path(RouteConfig.routeConfig.get("userServiceSwaggerPath"))
                        .filters(f -> f
                                .rewritePath(RouteConfig.routeConfig.get("userServiceSwaggerPattern"), RouteConfig.routeConfig.get("userServiceSwaggerReplacement")))
                        .uri(RouteConfig.routeConfig.get("userServiceUri"))
                )
                .route(RouteConfig.routeConfig.get("userServiceSwaggerApiDocsName"), r -> r.path(RouteConfig.routeConfig.get("userServiceSwaggerApiDocsPath"))
                        .filters(f -> f
                                .rewritePath(RouteConfig.routeConfig.get("userServiceSwaggerApiDocsPattern"), RouteConfig.routeConfig.get("userServiceSwaggerApiDocsReplacement")))
                        .uri(RouteConfig.routeConfig.get("userServiceUri"))
                )

                // === AUTH ===
                .route(RouteConfig.routeConfig.get("authServiceName"), r -> r.path(RouteConfig.routeConfig.get("authServicePath"))
                        .filters(f -> f
                                .rewritePath(RouteConfig.routeConfig.get("authServicePattern"), RouteConfig.routeConfig.get("authServiceReplacement")))
                        .uri(RouteConfig.routeConfig.get("authServiceUri"))
                )

                // === H2 AUTH ===
                .route(RouteConfig.routeConfig.get("authServiceH2Name"), r -> r.path(RouteConfig.routeConfig.get("authServiceH2Path"))
                        .filters(f -> f
                                .rewritePath(RouteConfig.routeConfig.get("authServiceH2Pattern"), RouteConfig.routeConfig.get("authServiceH2Replacement")))
                        .uri(RouteConfig.routeConfig.get("authServiceUri"))
                )

                // === SWAGGER AUTH ===
                .route(RouteConfig.routeConfig.get("authServiceSwaggerName"), r -> r.path(RouteConfig.routeConfig.get("authServiceSwaggerPath"))
                        .filters(f -> f
                                .rewritePath(RouteConfig.routeConfig.get("authServiceSwaggerPattern"), RouteConfig.routeConfig.get("authServiceSwaggerReplacement")))
                        .uri(RouteConfig.routeConfig.get("authServiceUri"))
                )
                .route(RouteConfig.routeConfig.get("authServiceSwaggerApiDocsName"), r -> r.path(RouteConfig.routeConfig.get("authServiceSwaggerApiDocsPath"))
                        .filters(f -> f
                                .rewritePath(RouteConfig.routeConfig.get("authServiceSwaggerApiDocsPattern"), RouteConfig.routeConfig.get("authServiceSwaggerApiDocsReplacement")))
                        .uri(RouteConfig.routeConfig.get("authServiceUri"))
                )

                .build();
    }

    @Bean
    protected SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers(WHITELIST).permitAll()
                        .pathMatchers("/user/signup").permitAll()
                        .pathMatchers("/auth/signin").permitAll()
                        .pathMatchers("/user/update").authenticated()
                        .pathMatchers("/user/get-by-uuid/**").authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(converterJwt -> {
                            Collection<GrantedAuthority> authorities = converterJwt.getClaimAsStringList("authorities").stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                            return  Mono.just(new JwtAuthenticationToken(converterJwt, authorities));
                        }))
                );
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        byte[] keyBytes = System.getenv("SECRET_KEY").getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA512");
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/nonexistent/");
    }
}
