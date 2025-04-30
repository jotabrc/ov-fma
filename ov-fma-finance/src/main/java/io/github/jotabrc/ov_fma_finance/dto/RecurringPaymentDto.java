package io.github.jotabrc.ov_fma_finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class RecurringPaymentDto extends RecurrenceDto {

    private final String payee;

    public RecurringPaymentDto(long id, BigDecimal amount, String description, LocalDate recurringUntil, String payee) {
        super(id, amount, description, recurringUntil);
        this.payee = payee;
    }
}
