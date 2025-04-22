package io.github.jotabrc.ov_fma_auth.config;

import io.github.jotabrc.ovauth.config.PropertiesWhitelistLoaderImpl;
import io.github.jotabrc.ovauth.jwt.TokenGlobalFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static io.github.jotabrc.ov_fma_auth.controller.ControllerPath.PREFIX;
import static io.github.jotabrc.ov_fma_auth.controller.ControllerPath.VERSION;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final LoadProperties loadProperties;
    private final String[] WHITELIST;

    @Autowired
    public WebSecurityConfig(LoadProperties loadProperties) {
        this.loadProperties = loadProperties;
        this.WHITELIST = PropertiesWhitelistLoaderImpl.WHITELIST.values().toArray(new String[0]);
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST).permitAll()
                        .requestMatchers(PREFIX + VERSION + "/auth/signin").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterAfter(new TokenGlobalFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
