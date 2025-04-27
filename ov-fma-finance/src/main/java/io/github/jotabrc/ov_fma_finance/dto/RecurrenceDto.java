package io.github.jotabrc.ov_fma_finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public abstract class RecurrenceDto extends FinancialEntityDto {

    private LocalDate recurringUntil;

    public RecurrenceDto(String userUuid, BigDecimal amount, String description, LocalDate recurringUntil) {
        super(userUuid, amount, description);
        this.recurringUntil = recurringUntil;
    }
}
