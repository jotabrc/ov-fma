package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public final class PaymentDto extends FinancialEntityDto implements Serializable {

    private final String payee;

    @JsonCreator
    public PaymentDto(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("dueDate") LocalDate dueDate,
            @JsonProperty("amount") double amount,
            @JsonProperty("description") String description,
            @JsonProperty("payee") String payee) {
        super(uuid, dueDate, amount, description);
        this.payee = payee;
    }
}
