package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class PaymentDto extends FinancialEntityDto implements Serializable {

    private final String payee;

    @JsonCreator
    public PaymentDto(
            @JsonProperty("id") long id,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("description") String description,
            @JsonProperty("payee") String payee) {
        super(id, amount, description);
        this.payee = payee;
    }
}
