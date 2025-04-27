package io.github.jotabrc.ov_fma_finance.dto;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "tb_recurring_receipt")
public class RecurringReceiptDto extends RecurrenceDto {

    private final PartyDto vendor;

    public RecurringReceiptDto(String userUuid, BigDecimal amount, String description, LocalDate recurringUntil, PartyDto vendor) {
        super(userUuid, amount, description, recurringUntil);
        this.vendor = vendor;
    }
}
