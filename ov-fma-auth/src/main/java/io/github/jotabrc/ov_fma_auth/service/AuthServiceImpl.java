package io.github.jotabrc.ov_fma_auth.service;

import io.github.jotabrc.ov_fma_auth.AuthRepository;
import io.github.jotabrc.ov_fma_auth.dto.UserDto;
import io.github.jotabrc.ov_fma_auth.handler.UserAlreadyExistsException;
import io.github.jotabrc.ov_fma_auth.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Auth Service implementation of AuthService interface.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Add new User from Kafka consumer event listener.
     * @param dto new User data.
     * @throws UserAlreadyExistsException if user already exists.
     */
    @Override
    public void add(final UserDto dto) {
        // Check if user already exists, if it does throw UserAlreadyExistsException.
        userExists(dto.getUuid());

        User user = buildNewUser(dto);
        authRepository.save(user);
    }

    /**
     * Updates User with Kafka consumer event listener data.
     * @param dto User updated data.
     */
    @Override
    public void update(final UserDto dto) {
        User user = getUser(dto.getUuid());
        updateUser(user, dto);
        authRepository.save(user);
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Check if user with uuid is already saved in the Auth service.
     * @param uuid UUID to check user existence.
     * @throws UserAlreadyExistsException if user already exists.
     */
    private void userExists(final String uuid) {
        boolean exists = authRepository.existsByUuid(uuid);
        if (exists) throw new UserAlreadyExistsException("User with UUID %s already exists".formatted(uuid));
    }

    /**
     * Creates User DAO for persistence.
     * @param dto User data.
     * @return User DAO.
     */
    private User buildNewUser(final UserDto dto) {
        return User
                .builder()
                .uuid(dto.getUuid())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .role(dto.getRole())
                .salt(dto.getSalt())
                .hash(dto.getHash())
                .isActive(dto.isActive())
                .build();
    }

    /**
     * Get User DAO with UUID.
     * @param uuid UUID to be searched.
     * @return User DAO.
     */
    private User getUser(final String uuid) {
        return authRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(uuid)));
    }

    /**
     * Updates User DAO with DTO data.
     * @param user User DAO to be updated.
     * @param dto User DTO with new data.
     */
    private void updateUser(final User user, final UserDto dto) {
        user
                .setUsername(dto.getUsername())
                .setEmail(dto.getEmail())
                .setRole(dto.getRole())
                .setSalt(dto.getSalt())
                .setHash(dto.getHash())
                .setActive(dto.isActive());
    }
}
