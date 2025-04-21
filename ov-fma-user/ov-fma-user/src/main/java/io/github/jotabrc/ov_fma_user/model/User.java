package io.github.jotabrc.ov_fma_user.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User Data Access Object.
 */
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_user")
public class User {

    // User relational ID
    // AUTO_INCREMENT
    // BIGINT
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Unique identifier for communication with other services
    @Column(length = 36, unique = true, nullable = false)
    private String uuid;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(length = 320, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    // User can have one role;
    // roles will be used to enforce RBAC;
    // Each user can have one role.
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    private String hash;

    // By default, all users upon registration are active;
    // inactive users cannot access the service.
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // Leveraging Hibernate auto generation of Timestamp;
    // Creates a timestamp on user persistence.
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Leveraging Hibernate auto generation of Timestamp;
    // Creates a timestamp on user update.
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Optimistic Lock property
    // Used by JPA to check if the data version is compatible checking the current version, if the value has changed
    // an OptimisticLockException is thrown, otherwise the transaction commits and increments the version value.
    @Version
    private long version;
}
