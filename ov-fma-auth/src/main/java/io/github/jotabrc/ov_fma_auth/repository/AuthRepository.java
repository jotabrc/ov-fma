package io.github.jotabrc.ov_fma_auth.repository;

import io.github.jotabrc.ov_fma_auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(final String username);

    Optional<User> findByUuid(final String uuid);
    boolean existsByUuid(final String uuid);
}
