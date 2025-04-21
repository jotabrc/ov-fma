package io.github.jotabrc.ov_fma_user.repository;

import io.github.jotabrc.ov_fma_user.model.Role;
import io.github.jotabrc.ov_fma_user.util.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(final RoleName name);
}
