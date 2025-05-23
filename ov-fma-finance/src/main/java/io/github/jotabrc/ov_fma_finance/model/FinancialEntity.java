package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@Entity(name = "tb_financial_entity")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FinancialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 36, nullable = false, unique = true)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "user_finance_id", nullable = false)
    private UserFinance userFinance;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(precision = 12, scale = 2, nullable = false)
    private double amount;

    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private long version;
}
