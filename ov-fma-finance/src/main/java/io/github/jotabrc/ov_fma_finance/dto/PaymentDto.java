package io.github.jotabrc.ov_fma_finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentDto extends FinancialEntityDto {

    private final String payee;

    public PaymentDto(long id, BigDecimal amount, String description, String payee) {
        super(id, amount, description);
        this.payee = payee;
    }
}
