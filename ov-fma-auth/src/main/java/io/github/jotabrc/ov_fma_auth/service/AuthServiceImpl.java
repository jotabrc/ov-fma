package io.github.jotabrc.ov_fma_auth.service;

import io.github.jotabrc.ov_fma_auth.AuthRepository;
import io.github.jotabrc.ov_fma_auth.config.RedisConfig;
import io.github.jotabrc.ov_fma_auth.dto.LoginDto;
import io.github.jotabrc.ov_fma_auth.dto.UserDto;
import io.github.jotabrc.ov_fma_auth.handler.AuthenticationDeniedException;
import io.github.jotabrc.ov_fma_auth.handler.TooManyRequestsException;
import io.github.jotabrc.ov_fma_auth.handler.UserAlreadyExistsException;
import io.github.jotabrc.ov_fma_auth.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_auth.model.User;
import io.github.jotabrc.ovauth.jwt.TokenConfig;
import io.github.jotabrc.ovauth.jwt.TokenCreator;
import io.github.jotabrc.ovauth.jwt.TokenObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Auth Service implementation of AuthService interface.
 */
@Service
public final class AuthServiceImpl implements AuthService {

    private final RedisConfig redisConfig;
    private final AuthRepository authRepository;

    @Autowired
    public AuthServiceImpl(RedisConfig redisConfig, AuthRepository authRepository) {
        this.redisConfig = redisConfig;
        this.authRepository = authRepository;
    }

    /**
     * Add new User from Kafka consumer event listener.
     *
     * @param dto new User data.
     * @throws UserAlreadyExistsException if user already exists.
     */
    @Override
    public void save(final UserDto dto) {
        // Check if user already exists, if it does throw UserAlreadyExistsException.
        userExists(dto.getUuid());
        User user = buildNewUser(dto);
        authRepository.save(user);
    }

    /**
     * Updates User with Kafka consumer event listener data.
     *
     * @param dto User updated data.
     */
    @Override
    public void update(final UserDto dto) {
        User user = getUserByUuid(dto.getUuid());
        updateUser(user, dto);
        authRepository.save(user);
    }

    /**
     * Check user's credentials for authentication.
     *
     * @param dto User credentials.
     * @return JWT Token.
     */
    @Override
    public String login(final String userUuid, final LoginDto dto) throws NoSuchAlgorithmException {
        doCache(userUuid);

        User user = getUserByUsername(dto.getUsername());
        validateCredentials(dto, user);

        return createJwtToken(user.getUuid(), user.getRole());
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Check if user with uuid is already saved in the Auth service.
     *
     * @param uuid UUID to check user existence.
     * @throws UserAlreadyExistsException if user already exists.
     */
    private void userExists(final String uuid) {
        boolean exists = authRepository.existsByUuid(uuid);
        if (exists) throw new UserAlreadyExistsException("User with UUID %s already exists".formatted(uuid));
    }

    /**
     * Creates User DAO for persistence.
     *
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
     *
     * @param uuid UUID to be searched.
     * @return User DAO.
     */
    private User getUserByUuid(final String uuid) {
        return authRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(uuid)));
    }

    /**
     * Get User DAO with Username.
     *
     * @param username Username to be searched.
     * @return User DAO.
     */
    private User getUserByUsername(final String username) {
        return authRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username %s not found".formatted(username)));
    }

    /**
     * Updates User DAO with DTO data.
     *
     * @param user User DAO to be updated.
     * @param dto  User DTO with new data.
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

    /**
     * Encode salt byte[] to String.
     *
     * @param salt byte[]
     * @return salt String
     * @throws NoSuchAlgorithmException If salt or hash encryption algorithm is not found.
     */
    private String getEncodedSalt(final byte[] salt) throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Creates random byte[] salt.
     *
     * @return byte[].
     * @throws NoSuchAlgorithmException if salt or hash encryption algorithm is not found.
     */
    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[32];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * Creates hash.
     *
     * @param password String password to be hashed.
     * @param salt     byte[] salt to hash password.
     * @return String hash.
     * @throws NoSuchAlgorithmException if salt or hash encryption algorithm is not found.
     */
    private String getHash(final String password, final byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    /**
     * Creates hash using String password and String salt, transform String salt in byte[], and;
     * calls getHash(String, byte[]).
     *
     * @param password String.
     * @param salt     String.
     * @return hashed String.
     * @throws NoSuchAlgorithmException if salt or hash encryption algorithm is not found.
     */
    private String getHash(final String password, final String salt) throws NoSuchAlgorithmException {
        byte[] saltByte = Base64.getDecoder().decode(salt);
        return getHash(password, saltByte);
    }

    /**
     * Validate User credentials.
     *
     * @param dto  User information to be validated.
     * @param user Valid user credentials.
     * @throws NoSuchAlgorithmException hash and salt algorithm error.
     */
    private void validateCredentials(LoginDto dto, User user) throws NoSuchAlgorithmException {
        String hashedPassword = getHash(dto.getPassword(), user.getSalt());
        if (!hashedPassword.equals(user.getHash()))
            throw new AuthenticationDeniedException("Authentication denied, credentials don't match");
    }

    /**
     * Creates JWT Token.
     * @param uuid Token subject.
     * @return JWT String.
     */
    private String createJwtToken(final String uuid, String role) {
        TokenObject tokenObject = TokenObject
                .builder()
                .subject(uuid)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TokenConfig.EXPIRATION))
                .roles(List.of(role))
                .build();

        return TokenCreator.create(TokenConfig.PREFIX, TokenConfig.KEY, tokenObject);
    }

    /**
     * Cache user attempts to authenticate.
     * @param userUuid
     */
    private void doCache(final String userUuid) {
        Boolean firstAttempt = redisConfig.redisTemplate().opsForValue().setIfAbsent(userUuid, 1, Duration.ofMinutes(10));

        Long tries = 1L;
        if (Boolean.FALSE.equals(firstAttempt)) tries = redisConfig.redisTemplate().opsForValue().increment(userUuid, 1);
        if (tries == null) tries = 1L;
        else if (tries.compareTo(4L) >= 0) throw new TooManyRequestsException("Too many requests, wait before trying again");
    }
}
