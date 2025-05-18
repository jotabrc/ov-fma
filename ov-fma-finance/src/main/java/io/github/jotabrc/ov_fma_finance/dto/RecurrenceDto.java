package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public abstract class RecurrenceDto extends FinancialEntityDto implements Serializable {

    private final int day;
    private final int month;
    private final int year;

    @JsonCreator
    public RecurrenceDto(
            @JsonProperty("id") long id,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("description") String description,
            @JsonProperty("day") int day,
            @JsonProperty("month") int month,
            @JsonProperty("year") int year) {
        super(id, amount, description);
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
