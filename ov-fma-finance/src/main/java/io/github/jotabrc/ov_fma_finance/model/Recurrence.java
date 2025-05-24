package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "tb_recurrence")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        indexes = {
                @Index(name = "idx_recurring_until", columnList = "recurring_until")
        }
)
public abstract class Recurrence extends FinancialEntity {

    @Column(nullable = false, name = "recurrence_until")
    private LocalDate recurringUntil;


    public Recurrence(long id,
                      String uuid,
                      UserFinance userFinance,
                      LocalDate dueDate,
                      double amount,
                      String description,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      long version,
                      LocalDate recurringUntil) {
        super(id, uuid, userFinance, dueDate, amount, description, createdAt, updatedAt, version);
        this.recurringUntil = recurringUntil;
    }
}
