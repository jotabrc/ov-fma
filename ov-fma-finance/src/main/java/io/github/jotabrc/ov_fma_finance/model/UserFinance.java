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
public final class UserFinance implements ToDto<UserFinanceDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_uuid", length = 36, nullable = false, unique = true)
    private String userUuid;

    @Column(nullable = false)
    private String name;

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
                .name(this.getName())
                .financialItems(
                        this.getFinancialItems()
                                .stream()
                                .map(FinancialEntity::transform)
                                .toList()
                )
                .build();
    }
}
