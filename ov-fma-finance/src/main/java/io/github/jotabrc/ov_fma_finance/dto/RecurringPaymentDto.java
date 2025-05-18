package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class RecurringPaymentDto extends RecurrenceDto implements Serializable {

    private final String payee;

    @JsonCreator
    public RecurringPaymentDto(
            @JsonProperty("id") long id,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("description") String description,
            @JsonProperty("day") int day,
            @JsonProperty("month") int month,
            @JsonProperty("year") int year,
            @JsonProperty("payee") String payee) {
        super(id, amount, description, day, month, year);
        this.payee = payee;
    }
}
