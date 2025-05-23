package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public abstract class FinancialEntityDto implements Serializable {

    private final String uuid;
    private final LocalDate dueDate;
    private final double amount;
    private final String description;

    @JsonCreator
    public FinancialEntityDto(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("dueDate") LocalDate dueDate,
            @JsonProperty("amount") double amount,
            @JsonProperty("amount") String description) {
        this.uuid = uuid;
        this.dueDate = dueDate;
        this.amount = amount;
        this.description = description;
    }
}
