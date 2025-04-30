package io.github.jotabrc.ov_fma_finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public abstract class RecurrenceDto extends FinancialEntityDto {

    private final LocalDate recurringUntil;

    public RecurrenceDto(long id, BigDecimal amount, String description, LocalDate recurringUntil) {
        super(id, amount, description);
        this.recurringUntil = recurringUntil;
    }
}
