package io.github.jotabrc.ov_fma_finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReceiptDto extends FinancialEntityDto {

    private final PartyDto payee;

    public ReceiptDto(String userUuid, BigDecimal amount, String description, PartyDto payee) {
        super(userUuid, amount, description);
        this.payee = payee;
    }
}
