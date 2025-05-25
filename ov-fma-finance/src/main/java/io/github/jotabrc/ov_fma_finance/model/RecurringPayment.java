package io.github.jotabrc.ov_fma_finance.model;

import io.github.jotabrc.ov_fma_finance.dto.FinancialEntityDto;
import io.github.jotabrc.ov_fma_finance.dto.RecurringPaymentDto;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "tb_recurring_payment")
public final class RecurringPayment extends Recurrence {

    private String payee;

    public RecurringPayment(long id, String uuid, UserFinance userFinance, LocalDate dueDate, double amount, String description, LocalDateTime createdAt, LocalDateTime updatedAt, long version, LocalDate recurringUntil, String payee) {
        super(id, uuid, userFinance, dueDate, amount, description, createdAt, updatedAt, version, recurringUntil);
        this.payee = payee;
    }

    @Override
    public FinancialEntityDto transform() {
        return new RecurringPaymentDto(
                this.getUuid(),
                this.getDueDate(),
                this.getAmount(),
                this.getDescription(),
                this.getRecurringUntil(),
                this.getPayee()
        );
    }
}
