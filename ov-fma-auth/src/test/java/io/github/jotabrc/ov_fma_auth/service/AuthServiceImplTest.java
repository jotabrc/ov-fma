package io.github.jotabrc.ov_fma_auth.service;

import io.github.jotabrc.ov_fma_auth.config.RedisConfig;
import io.github.jotabrc.ov_fma_auth.dto.LoginDto;
import io.github.jotabrc.ov_fma_auth.dto.UserDto;
import io.github.jotabrc.ov_fma_auth.model.User;
import io.github.jotabrc.ov_fma_auth.repository.AuthRepository;
import io.github.jotabrc.ovauth.jwt.TokenConfig;
import io.github.jotabrc.ovauth.jwt.TokenCreator;
import io.github.jotabrc.ovauth.jwt.TokenObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private RedisConfig redisConfig;

    @Mock
    private AuthRepository authRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        UserDto dto = new UserDto(
                UUID.randomUUID().toString(),
                "username",
                "email@email.com",
                "USER",
                "salt",
                "hash",
                true
        );
        when(authRepository.existsByUuid(any())).thenReturn(false);
        when(authRepository.save(any())).thenReturn(any());
        authService.save(dto);
    }

    @Test
    void update() {
        UserDto dto = new UserDto(
                UUID.randomUUID().toString(),
                "username",
                "email@email.com",
                "USER",
                "salt",
                "hash",
                true
        );
        User user = new User(
                1,
                dto.getUuid(),
                "oldUsername",
                "oldemail@email.com",
                "USER",
                "oldsalt",
                "oldhash",
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                0
        );
        when(authRepository.findByUuid(any())).thenReturn(Optional.of(user));
        when(authRepository.save(any())).thenReturn(any());
        authService.update(dto);
        assert user.getSalt().equals(dto.getSalt());
        assert user.getHash().equals(dto.getHash());
        assert user.getUsername().equals(dto.getUsername());
        assert user.getEmail().equals(dto.getEmail());
    }

    @Test
    void login() throws NoSuchAlgorithmException {
        LoginDto dto = new LoginDto(
                "username",
                "password1234"
        );
        User user = new User(
                1,
                UUID.randomUUID().toString(),
                "username",
                "email@email.com",
                "USER",
                "salt",
                "WC6CSqWrJ0CZBmWfR6oaDJFpgKiO/rhBhsNPGC2tAhEkN07JcdxGm7yCU7jIxF9V8LjJoik+4u2kkJ5mjmZw8Q==",
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                0
        );
        when(redisConfig.redisTemplate()).thenReturn(redisTemplate);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(any(),any(),any())).thenReturn(true);
        when(authRepository.findByUsername(any())).thenReturn(Optional.of(user));
        try (MockedStatic<TokenCreator> mockedStatic = mockStatic(TokenCreator.class)) {
            TokenConfig.EXPIRATION = 100L;
            TokenConfig.KEY = "key";
            mockedStatic.when(() -> TokenCreator.create(any(), any(String.class), any(TokenObject.class)))
                    .thenReturn("token");
            var result = authService.login(user.getUuid(), dto);
            assert result.equals("token");
        }
        // TokenConfig requires final fields
        // Change shared library to use application properties
    }
}