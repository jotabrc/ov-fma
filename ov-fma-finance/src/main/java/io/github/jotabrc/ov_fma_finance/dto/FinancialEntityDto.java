package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public abstract class FinancialEntityDto implements Serializable {

    private final long id;
    private final BigDecimal amount;
    private final String description;

    @JsonCreator
    public FinancialEntityDto(
            @JsonProperty("id") long id,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("amount") String description) {
        this.id = id;
        this.amount = amount;
        this.description = description;
    }
}
