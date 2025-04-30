package io.github.jotabrc.ov_fma_finance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public abstract class FinancialEntityDto {

    private final long id;
    private final BigDecimal amount;
    private final String description;
}
