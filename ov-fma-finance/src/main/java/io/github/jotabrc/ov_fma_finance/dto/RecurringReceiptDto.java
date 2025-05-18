package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class RecurringReceiptDto extends RecurrenceDto implements Serializable {

    private final String vendor;

    @JsonCreator
    public RecurringReceiptDto(
            @JsonProperty("id") long id,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("description") String description,
            @JsonProperty("day") int day,
            @JsonProperty("month") int month,
            @JsonProperty("year") int year,
            @JsonProperty("payee") String vendor) {
        super(id, amount, description, day, month, year);
        this.vendor = vendor;
    }
}
