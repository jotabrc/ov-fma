package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "tb_recurring_receipt")
public class RecurringReceipt extends Recurrence {

    private String vendor;

    public RecurringReceipt(long id, String uuid, UserFinance userFinance, LocalDate dueDate, double amount, String description, LocalDateTime createdAt, LocalDateTime updatedAt, long version, LocalDate recurringUntil, String vendor) {
        super(id, uuid, userFinance, dueDate, amount, description, createdAt, updatedAt, version, recurringUntil);
        this.vendor = vendor;
    }
}
