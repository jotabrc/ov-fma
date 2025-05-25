package io.github.jotabrc.ov_fma_user.config;

import io.github.jotabrc.ov_fma_user.util.LoadProperties;
import io.github.jotabrc.ov_fma_user.util.RoleName;
import io.github.jotabrc.ovauth.config.PropertiesWhitelistLoaderImpl;
import io.github.jotabrc.ovauth.jwt.TokenGlobalFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static io.github.jotabrc.ov_fma_user.controller.ControllerPath.PREFIX;
import static io.github.jotabrc.ov_fma_user.controller.ControllerPath.VERSION;

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
                .httpBasic(Customizer.withDefaults())
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST).permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(PREFIX + VERSION + "/user/login").permitAll()
                        .requestMatchers(PREFIX + VERSION + "/user/update/**").hasAnyRole(RoleName.USER.getName(), RoleName.ADMIN.getName())
                        .requestMatchers(PREFIX + VERSION + "/user/get/**").hasAnyRole(RoleName.USER.getName(), RoleName.ADMIN.getName())
                        .anyRequest().authenticated()
                )
                .addFilterAfter(new TokenGlobalFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
