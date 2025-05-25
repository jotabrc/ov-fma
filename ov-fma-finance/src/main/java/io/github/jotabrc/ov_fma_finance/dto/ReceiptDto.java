package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public final class ReceiptDto extends FinancialEntityDto implements Serializable {

    private final String vendor;

    @JsonCreator
    public ReceiptDto(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("dueDate") LocalDate dueDate,
            @JsonProperty("amount") double amount,
            @JsonProperty("description") String description,
            @JsonProperty("vendor") String vendor) {
        super(uuid, dueDate, amount, description);
        this.vendor = vendor;
    }
}
