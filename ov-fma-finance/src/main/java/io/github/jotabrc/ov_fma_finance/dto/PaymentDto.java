package io.github.jotabrc.ov_fma_finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentDto extends FinancialEntityDto {

    private final String vendor;

    public PaymentDto(String userUuid, BigDecimal amount, String description, String vendor) {
        super(userUuid, amount, description);
        this.vendor = vendor;
    }
}
