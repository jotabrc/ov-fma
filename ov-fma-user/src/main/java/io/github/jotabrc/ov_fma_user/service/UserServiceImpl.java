package io.github.jotabrc.ov_fma_user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.ov_fma_user.config.KafkaTopic;
import io.github.jotabrc.ov_fma_user.dto.*;
import io.github.jotabrc.ov_fma_user.handler.AuthorizationDeniedException;
import io.github.jotabrc.ov_fma_user.handler.CredentialNotAvailableException;
import io.github.jotabrc.ov_fma_user.handler.RoleNotFoundException;
import io.github.jotabrc.ov_fma_user.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_user.model.Role;
import io.github.jotabrc.ov_fma_user.model.User;
import io.github.jotabrc.ov_fma_user.repository.RoleRepository;
import io.github.jotabrc.ov_fma_user.repository.UserRepository;
import io.github.jotabrc.ov_fma_user.util.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User Service class implementation of UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, KafkaProducer kafkaProducer) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * User registration service method.
     * Requires unique EMAIL and USERNAME.
     *
     * @param dto User data to be persisted and validated.
     * @return UUID if registration is successful.
     */
    @Override
    public String signup(final UserCreationUpdateDto dto) throws NoSuchAlgorithmException, JsonProcessingException {

        // Throws CredentialNotAvailableException if email or username is already in use.
        emailAndUsernameAvailability(dto.getEmail(), dto.getUsername());
        // build UserDao to be persisted
        User user = buildNewUser(dto);
        userRepository.save(user);

        // Sends Kafka message to Authentication service
        sendKafkaMessageToAuthService(user, KafkaTopic.USER_NEW);
        // Sends Kafka message to Finance service
        sendKafkaMessageToFinanceService(user, KafkaTopic.USER_FINANCE_NEW);

        return user.getUuid();
    }

    /**
     * Updates user information, update password if NOT null/empty/blank.
     *
     * @param dto UserCreationUpdateDto.
     */
    @CachePut(value = "users", key = "#uuid")
    @Override
    public UserDto update(final String uuid, final UserCreationUpdateDto dto) throws NoSuchAlgorithmException, JsonProcessingException {
        // Checks if account to be changed is the same as the token authentication
        checkUserAuthorization(uuid);
        // Throws UserNotFoundException if user UUID is not found.
        User user = getUser(dto.getUuid());
        // Throws CredentialNotAvailableException if email or username is already in use.
        dataToBeChecked(dto, user);

        // Update user data.
        updateUserData(dto, user);

        // Sends Kafka message to Authentication service
        // User will be updated
        sendKafkaMessageToAuthService(user, KafkaTopic.USER_UPDATE);
        // Sends Kafka message to Finance service
        // User will be updated
        sendKafkaMessageToFinanceService(user, KafkaTopic.USER_FINANCE_UPDATE);

        userRepository.save(user);
        return toDto(user);
    }

    /**
     * Get User by UUID.
     *
     * @param uuid uuid String to search user.
     * @return UserDto.
     * @throws UserNotFoundException if no user is found with the provided UUID parameter.
     */
    @Cacheable(value = "users", key = "#uuid")
    @Override
    public UserDto getByUuid(String uuid) throws UserNotFoundException {
        // Checks if account requested user information matched authenticated user
        checkUserAuthorization(uuid);
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(uuid)));
        return toDto(user);
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Find user by UUID
     *
     * @param uuid User UUID.
     * @return User DAO.
     */
    private User getUser(final String uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(uuid)));
    }

    /**
     * Check if user email and/or username should be updated.
     *
     * @param dto  New User data.
     * @param user Current user data.
     */
    private void dataToBeChecked(final UserCreationUpdateDto dto, final User user) {
        boolean newEmail = false;
        boolean newUsername = false;

        if (!user.getEmail().equals(dto.getEmail())) newEmail = true;
        if (!user.getUsername().equals(dto.getUsername())) newUsername = true;

        // Throws CredentialNotAvailableException if email or username is already in use.
        if (newEmail && newUsername) emailAndUsernameAvailability(dto.getEmail(), dto.getUsername());
        else if (newEmail) emailAvailability(dto.getEmail());
        else if (newUsername) usernameAvailability(dto.getUsername());
    }

    /**
     * Updates password creating new salt and hashing the dto password.
     *
     * @param password to be checked and hashed.
     * @param user     Current user data.
     * @return New hash and salt if password needed to change.
     * @throws NoSuchAlgorithmException if salt or hash encryption algorithm is not found.
     */
    private NewHashAndSalt updatePassword(final String password, final User user) throws NoSuchAlgorithmException {
        String salt = user.getSalt();
        String hash = user.getHash();

        if (password != null && !password.isEmpty()) {
            byte[] newSalt = getSalt();
            String encodedSalt = getEncodedSalt(newSalt);
            String newHash = getHash(password, newSalt);

            salt = encodedSalt;
            hash = newHash;
        }
        return new NewHashAndSalt(salt, hash);
    }

    /**
     * Record for updatePassword method.
     *
     * @param salt
     * @param hash
     */
    private record NewHashAndSalt(String salt, String hash) {
    }

    /**
     * Update user data and create new password if required.
     *
     * @param dto  New user data.
     * @param user current user data.
     * @return updated User.
     * @throws NoSuchAlgorithmException if salt or hash encryption algorithm is not found.
     */
    private void updateUserData(final UserCreationUpdateDto dto, final User user) throws NoSuchAlgorithmException {
        // Create new salt and hash if password in UserCreationUpdateDto is not null or empty, or return those in User DAO.
        NewHashAndSalt result = updatePassword(dto.getPassword(), user);

        user
                .setName(dto.getName())
                .setUsername(dto.getUsername())
                .setEmail(dto.getEmail())
                .setHash(result.hash())
                .setSalt(result.salt());
    }

    /**
     * Build User DAO for persistence.
     *
     * @param dto UserCreationUpdateDto.
     * @return User DAO.
     * @throws NoSuchAlgorithmException if salt or hash encryption algorithm is not found.
     */
    private User buildNewUser(final UserCreationUpdateDto dto) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        String encodedSalt = getEncodedSalt(salt);
        String hash = getHash(dto.getPassword(), salt);

        // Throws RoleNotFoundException if Role USER is not found
        Role role = getRoleUser();

        return User
                .builder()
                .uuid(UUID.randomUUID().toString())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .name(dto.getName())
                .role(role)
                .isActive(true)
                .hash(hash)
                .salt(encodedSalt)
                .build();
    }

    /**
     * Checks if Username and/or Email is already in use.
     *
     * @param email    User email.
     * @param username Username.
     */
    private void emailAndUsernameAvailability(final String email, final String username) {
        List<User> users = userRepository.findByEmailOrUsername(email, username);
        AtomicBoolean emailInUse = new AtomicBoolean(false);
        AtomicBoolean usernameInUse = new AtomicBoolean(false);
        if (!users.isEmpty()) {
            users.forEach(user -> {
                if (!emailInUse.get() && user.getEmail().equals(email)) emailInUse.set(true);
                if (!usernameInUse.get() && user.getUsername().equals(username)) usernameInUse.set(true);
            });
            throw new CredentialNotAvailableException("Email %s already in use: %s, Username %s already in use: %s"
                    .formatted(email, emailInUse, username, usernameInUse));
        }
    }

    /**
     * Checks if Username is already in use.
     *
     * @param username Username.
     * @throws CredentialNotAvailableException If Username is already in use.
     */
    private void usernameAvailability(final String username) {
        List<User> users = userRepository.findByUsername(username);
        if (!users.isEmpty())
            throw new CredentialNotAvailableException("Username %s already in use"
                    .formatted(username));
    }

    /**
     * Checks if Email is already in use.
     *
     * @param email User email.
     * @throws CredentialNotAvailableException If Email is already in use.
     */
    private void emailAvailability(final String email) {
        List<User> users = userRepository.findByEmail(email);
        if (!users.isEmpty())
            throw new CredentialNotAvailableException("Email %s already in use"
                    .formatted(email));
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
     * Search Role in database by name.
     *
     * @return Role.
     * @throws RoleNotFoundException if role is not found.
     */
    private Role getRoleUser() {
        return roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RoleNotFoundException("Role USER not found"));
    }

    /**
     * Transform User into UserDto.
     *
     * @param user User DAO.
     * @return UserDto.
     */
    private UserDto toDto(final User user) {
        RoleDto roleDto = new RoleDto(user.getRole().getUuid(), user.getRole().getName().getName(), user.getRole().getDescription());
        return new UserDto(user.getUuid(), user.getUsername(), user.getEmail(), user.getName(), roleDto);
    }

    /**
     * Transforms User into UserKafkaDto.
     *
     * @param user User DAO.
     * @return UserKafkaDto.
     */
    private UserKafkaDto toKafkaDto(final User user) {
        return new UserKafkaDto(user.getUuid(), user.getUsername(), user.getEmail(), user.getRole().getName().getName(),
                user.getSalt(), user.getHash(), user.isActive());
    }

    /**
     * Transforms User into UserFinanceKafkaDto.
     *
     * @param user User DAO.
     * @return UserFinanceKafkaDto.
     */
    private UserFinanceKafkaDto toUserFinanceKafkaDto(final User user) {
        return new UserFinanceKafkaDto(user.getUuid(), user.getUsername(), user.getEmail(), user.isActive());
    }

    /**
     * Send Kafka Message.
     *
     * @param user User DAO will be transformed into UserKafkaDto.
     * @throws JsonProcessingException if DTO parsing to JSON error occurs.
     */
    private void sendKafkaMessageToAuthService(final User user, final String topic) throws JsonProcessingException {
        UserKafkaDto kafkaDto = toKafkaDto(user);
        kafkaProducer.produce(kafkaDto, topic);
    }

    /**
     * Send Kafka Message.
     *
     * @param user User DAO will be transformed into UserFinanceKafkaDto.
     * @throws JsonProcessingException if DTO parsing to JSON error occurs.
     */
    private void sendKafkaMessageToFinanceService(final User user, final String topic) throws JsonProcessingException {
        UserFinanceKafkaDto userFinanceKafkaDto = toUserFinanceKafkaDto(user);
        kafkaProducer.produce(userFinanceKafkaDto, topic);
    }

    /**
     * Checks SecurityContextHolder Authentication for matching UUID.
     * @param uuid UUID to be checked.
     */
    private void checkUserAuthorization(final String uuid) {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals(uuid))
            throw new AuthorizationDeniedException("Security Context Holder authentication doesn't match with provided user information");
    }
}
