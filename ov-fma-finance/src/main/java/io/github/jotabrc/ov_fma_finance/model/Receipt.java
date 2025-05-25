package io.github.jotabrc.ov_fma_finance.model;

import io.github.jotabrc.ov_fma_finance.dto.FinancialEntityDto;
import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "tb_receipt")
public final class Receipt extends FinancialEntity {

    private String vendor;

    public Receipt(long id, String uuid, UserFinance userFinance, LocalDate dueDate, double amount, String description, LocalDateTime createdAt,
                   LocalDateTime updatedAt, long version, String vendor) {
        super(id, uuid, userFinance, dueDate, amount, description, createdAt, updatedAt, version);
        this.vendor = vendor;
    }

    @Override
    public FinancialEntityDto transform() {
        return new ReceiptDto(
                this.getUuid(),
                this.getDueDate(),
                this.getAmount(),
                this.getDescription(),
                this.getVendor()
        );
    }
}
