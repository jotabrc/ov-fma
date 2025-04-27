package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "tb_recurrence")
public abstract class Recurrence extends FinancialEntity {

    @Column(name = "recurring_until", nullable = false)
    private LocalDate recurringUntil;

    public Recurrence(long id, String uuid, UserFinance userFinance, BigDecimal amount, String description, LocalDateTime createdAt,
                      LocalDateTime updatedAt, long version, LocalDate recurringUntil) {
        super(id, uuid, userFinance, amount, description, createdAt, updatedAt, version);
        this.recurringUntil = recurringUntil;
    }
}
