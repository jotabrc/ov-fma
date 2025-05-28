package io.github.jotabrc.ov_fma_user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.ov_fma_user.dto.UserCreationUpdateDto;
import io.github.jotabrc.ov_fma_user.dto.UserDto;
import io.github.jotabrc.ov_fma_user.model.Role;
import io.github.jotabrc.ov_fma_user.model.User;
import io.github.jotabrc.ov_fma_user.repository.RoleRepository;
import io.github.jotabrc.ov_fma_user.repository.UserRepository;
import io.github.jotabrc.ov_fma_user.util.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() throws JsonProcessingException, NoSuchAlgorithmException {
        UserCreationUpdateDto dto = new UserCreationUpdateDto(
                "username",
                "email@email.com",
                "John Doe",
                "password1234"
        );
        Role role = Role
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(RoleName.USER)
                .description("user")
                .isActive(true)
                .build();
        User user = User
                .builder()
                .username("username")
                .email("email@email.com")
                .name("John Doe")
                .salt("salt")
                .hash("hash")
                .role(role)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
        when(userRepository.findByEmailOrUsername(anyString(), anyString())).thenReturn(List.of());
        when(userRepository.save(any())).thenReturn(user);
        when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        doNothing().when(kafkaProducer).produce(any(), any());
        UserDto userDto = userService.save(dto);
        assert userDto.getName().equals(dto.getName());
        assert !userDto.getUuid().isEmpty();
    }

    @Test
    void update() throws JsonProcessingException, NoSuchAlgorithmException {
        UserCreationUpdateDto dto = new UserCreationUpdateDto(
                "newusername",
                "newemail@email.com",
                "John Bloggs",
                "password1234"
        );
        Role role = Role
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(RoleName.USER)
                .description("user")
                .isActive(true)
                .build();
        User user = User
                .builder()
                .uuid(UUID.randomUUID().toString())
                .username("username")
                .email("email@email.com")
                .name("John Doe")
                .salt("salt")
                .hash("hash")
                .role(role)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findByUuid(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(anyString())).thenReturn(List.of());
        when(userRepository.findByEmail(anyString())).thenReturn(List.of());
        doNothing().when(kafkaProducer).produce(any(), any());
        when(userRepository.save(user)).thenReturn(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getUuid());

        UserDto userDto = userService.update(user.getUuid(), dto);
        assert userDto.getName().equals(user.getName());
        assert userDto.getEmail().equals(user.getEmail());
        assert userDto.getUsername().equals(user.getUsername());
    }

    @Test
    void get() {
        Role role = Role
                .builder()
                .uuid(UUID.randomUUID().toString())
                .name(RoleName.USER)
                .description("user")
                .isActive(true)
                .build();
        User user = User
                .builder()
                .uuid(UUID.randomUUID().toString())
                .username("username")
                .email("email@email.com")
                .name("John Doe")
                .salt("salt")
                .hash("hash")
                .role(role)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(user.getUuid());
        when(userRepository.findByUuid(anyString())).thenReturn(Optional.of(user));

        UserDto dto = userService.get(user.getUuid());
        assert dto.getUuid().equals(user.getUuid());
    }
}