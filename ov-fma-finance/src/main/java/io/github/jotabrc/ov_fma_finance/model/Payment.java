package io.github.jotabrc.ov_fma_finance.model;

import io.github.jotabrc.ov_fma_finance.dto.FinancialEntityDto;
import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "tb_payment")
public final class Payment extends FinancialEntity {

    @Column(nullable = false, unique = false)
    private String payee;

    public Payment(long id, String uuid, UserFinance userFinance, LocalDate dueDate, double amount, String description, LocalDateTime createdAt,
                   LocalDateTime updatedAt, long version, String payee) {
        super(id, uuid, userFinance, dueDate, amount, description, createdAt, updatedAt, version);
        this.payee = payee;
    }

    @Override
    public FinancialEntityDto transform() {
        return new PaymentDto(
                this.getUuid(),
                this.getDueDate(),
                this.getAmount(),
                this.getDescription(),
                this.getPayee()
        );
    }
}
