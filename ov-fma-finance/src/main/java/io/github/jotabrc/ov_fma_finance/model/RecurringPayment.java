package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "tb_recurring_payment")
public class RecurringPayment extends Recurrence {

    private String payee;

    public RecurringPayment(long id, String uuid, UserFinance userFinance, BigDecimal amount, String description, LocalDateTime createdAt,
                            LocalDateTime updatedAt, long version, LocalDate recurringUntil, String payee) {
        super(id, uuid, userFinance, amount, description, createdAt, updatedAt, version, recurringUntil);
        this.payee = payee;
    }
}
