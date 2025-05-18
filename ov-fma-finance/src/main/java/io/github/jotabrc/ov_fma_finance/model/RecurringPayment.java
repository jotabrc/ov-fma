package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "tb_recurring_payment")
public class RecurringPayment extends Recurrence {

    private String payee;

    public RecurringPayment(long id, String uuid, UserFinance userFinance, BigDecimal amount, String description, LocalDateTime createdAt,
                            LocalDateTime updatedAt, long version, int day, int month, int year, String payee) {
        super(id, uuid, userFinance, amount, description, createdAt, updatedAt, version, day, month, year);
        this.payee = payee;
    }
}
