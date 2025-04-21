package io.github.jotabrc.ov_fma_user.repository;

import io.github.jotabrc.ov_fma_user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(final String uuid);

    List<User> findByEmailOrUsername(final String email, final String username);

    List<User> findByEmail(final String email);

    List<User> findByUsername(final String username);
}
