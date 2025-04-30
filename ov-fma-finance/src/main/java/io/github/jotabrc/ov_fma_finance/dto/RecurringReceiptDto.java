package io.github.jotabrc.ov_fma_finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class RecurringReceiptDto extends RecurrenceDto {

    private final String vendor;

    public RecurringReceiptDto(long id, BigDecimal amount, String description, LocalDate recurringUntil, String vendor) {
        super(id, amount, description, recurringUntil);
        this.vendor = vendor;
    }
}
