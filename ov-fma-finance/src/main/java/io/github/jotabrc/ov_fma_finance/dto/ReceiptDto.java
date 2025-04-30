package io.github.jotabrc.ov_fma_finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReceiptDto extends FinancialEntityDto {

    private final String vendor;

    public ReceiptDto(long id, BigDecimal amount, String description, String vendor) {
        super(id, amount, description);
        this.vendor = vendor;
    }
}
