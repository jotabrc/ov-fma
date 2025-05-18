package io.github.jotabrc.ov_config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            String defaultUser = System.getenv("SPRING_USER");
            String defaultPassword = System.getenv("SPRING_PASSWORD");

            if (defaultUser == null || defaultPassword == null)
                throw new IllegalStateException("Environment variables SPRING_USER and SPRING_PASSWORD must be set");

            if (defaultUser.equals(username)) {
                return User
                        .builder()
                        .username(defaultUser)
                        .password("{noop}" + defaultPassword)
                        .build();
            }
            throw new RuntimeException("No user with username %s was found".formatted(username));
        };
    }
}
