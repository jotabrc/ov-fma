package io.github.jotabrc.ov_fma_auth.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User Data Access Entity
 */
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_user")
@Table(indexes = {
        @Index(name = "idx_user_username", columnList = "username", unique = true),
        @Index(name = "idx_user_email", columnList = "email", unique = true),
        @Index(name = "idx_user_uuid", columnList = "uuid", unique = true)
})
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

    @Column(length = 50, nullable = false)
    private String role;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    private String hash;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private long version;
}
