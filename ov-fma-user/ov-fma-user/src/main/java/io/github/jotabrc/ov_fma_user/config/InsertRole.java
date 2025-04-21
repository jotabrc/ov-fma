package io.github.jotabrc.ov_fma_user.config;

import io.github.jotabrc.ov_fma_user.model.Role;
import io.github.jotabrc.ov_fma_user.repository.RoleRepository;
import io.github.jotabrc.ov_fma_user.util.RoleName;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Order(1)
@Component
public class InsertRole implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public InsertRole(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = List.of(
                Role
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name(RoleName.USER)
                        .description("Active User")
                        .isActive(true)
                        .build(),
                Role
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .name(RoleName.ADMIN)
                        .description("Admin")
                        .isActive(true)
                        .build()
        );

        roles.forEach(roleRepository::save);
    }
}
