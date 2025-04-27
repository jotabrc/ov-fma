package io.github.jotabrc.ov_fma_finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentDto extends FinancialEntityDto {

    private PartyDto vendor;

    public PaymentDto(String userUuid, BigDecimal amount, String description, PartyDto vendor) {
        super(userUuid, amount, description);
        this.vendor = vendor;
    }
}
