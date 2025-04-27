package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "tb_recurring_receipt")
public class RecurringReceipt extends Recurrence {

    private Party vendor;

    public RecurringReceipt(long id, String uuid, UserFinance userFinance, BigDecimal amount, String description, LocalDateTime createdAt,
                            LocalDateTime updatedAt, long version, LocalDate recurringUntil, Party vendor) {
        super(id, uuid, userFinance, amount, description, createdAt, updatedAt, version, recurringUntil);
        this.vendor = vendor;
    }
}
