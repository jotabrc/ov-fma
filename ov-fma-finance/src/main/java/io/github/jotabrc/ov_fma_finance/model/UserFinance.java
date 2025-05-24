package io.github.jotabrc.ov_fma_finance.model;

import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import io.github.jotabrc.ov_fma_finance.util.ToDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_user_finance")
public class UserFinance implements ToDto<UserFinanceDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_uuid", length = 36, nullable = false, unique = true)
    private String userUuid;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(length = 320, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "userFinance", cascade = CascadeType.ALL)
    private List<FinancialEntity> financialItems;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private long version;

    @Override
    public UserFinanceDto transform() {
        return UserFinanceDto
                .builder()
                .userUuid(this.getUserUuid())
                .username(this.getUsername())
                .email(this.getEmail())
                .name(this.getName())
                .isActive(this.isActive())
                .financialItems(
                        this.getFinancialItems()
                                .stream()
                                .map(FinancialEntity::transform)
                                .toList()
                )
                .build();
    }
}
