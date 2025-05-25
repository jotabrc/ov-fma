package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public sealed abstract class RecurrenceDto extends FinancialEntityDto implements Serializable permits RecurringPaymentDto, RecurringReceiptDto {

    private final LocalDate recurringUntil;

    @JsonCreator
    public RecurrenceDto(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("dueDate") LocalDate dueDate,
            @JsonProperty("amount") double amount,
            @JsonProperty("description") String description,
            @JsonProperty("recurringUntil") LocalDate recurringUntil) {
        super(uuid, dueDate, amount, description);
        this.recurringUntil = recurringUntil;
    }
}
