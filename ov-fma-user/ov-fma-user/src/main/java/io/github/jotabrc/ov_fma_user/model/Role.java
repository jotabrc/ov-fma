package io.github.jotabrc.ov_fma_user.model;

import io.github.jotabrc.ov_fma_user.util.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Role Data Access Object.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_role")
public class Role {

    // Role relational ID
    // AUTO_INCREMENT
    // BIGINT
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 36, unique = true, nullable = false)
    private String uuid;

    // Role name uses RoleName enum
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false, unique = true)
    private RoleName name;

    // Role description is not required but appreciated
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // Leveraging Hibernate auto generation of Timestamp;
    // Creates a timestamp on role creation.
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Leveraging Hibernate auto generation of Timestamp;
    // Creates a timestamp on role update.
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Optimistic Lock property
    // Used by JPA to check if the data version is compatible checking the current version, if the value has changed
    // an OptimisticLockException is thrown, otherwise the transaction commits and increments the version value.
    @Version
    private long version;
}
